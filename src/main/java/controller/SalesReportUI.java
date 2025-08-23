package controller;

import dao.impl.DrugDAOImpl;
import dao.impl.MemberDAOImpl;
import dao.impl.SalesDAOImpl;
import dao.impl.EmployeeDAOImpl;
import model.Drug;
import model.Employee;
import model.Member;
import model.Sale;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesReportUI extends JPanel {
	private static final long serialVersionUID = 1L;
    private SalesDAOImpl salesDAO = new SalesDAOImpl();
    private MemberDAOImpl memberDAO = new MemberDAOImpl();
    private DrugDAOImpl drugDAO = new DrugDAOImpl();
    private EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();

    private Employee loggedInEmployee;

    private ChartPanel barChartPanel;
    private ChartPanel pieChartPanel;
    private JTable table;
    private DefaultTableModel tableModel;

    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton btnFilter;
    private JButton btnExport;

    public SalesReportUI(Employee loggedInEmployee) {
        setBackground(new Color(255, 128, 192));
        this.loggedInEmployee = loggedInEmployee;
        setLayout(null);

        // 日期範圍與篩選
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 128, 192));
        topPanel.setBounds(0, 0, 850, 33);
        topPanel.add(new JLabel("起始日期:"));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        topPanel.add(startDateSpinner);

        topPanel.add(new JLabel("結束日期:"));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        topPanel.add(endDateSpinner);

        btnFilter = new JButton("查詢");
        topPanel.add(btnFilter);

        btnExport = new JButton("匯出 Excel");
        topPanel.add(btnExport);

        add(topPanel);

        // 明細表格
        tableModel = new DefaultTableModel(new Object[]{"員工","會員","藥品","數量","單價","總額","時間"},0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 275, 850, 165);
        add(scrollPane);

        // 圖表區
        barChartPanel = new ChartPanel(null);
        barChartPanel.setBounds(0, 58, 420, 207);
        add(barChartPanel);

        pieChartPanel = new ChartPanel(null);
        pieChartPanel.setBounds(430, 58, 420, 207);
        add(pieChartPanel);

        // 事件
        btnFilter.addActionListener(e -> updateChartsAndTable());
        btnExport.addActionListener(e -> exportExcel());

        updateChartsAndTable();
    }

    private void updateChartsAndTable() {
        LocalDate startDate = ((java.util.Date)startDateSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = ((java.util.Date)endDateSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        List<Sale> sales = salesDAO.findAll().stream()
                .filter(s -> {
                    LocalDate d = s.getCreatedAt().toLocalDate();
                    return (!d.isBefore(startDate)) && (!d.isAfter(endDate));
                })
                .collect(Collectors.toList());

        // 長條圖: 每位員工銷售總額
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        Map<Integer, Double> employeeSales = sales.stream()
                .collect(Collectors.groupingBy(Sale::getEmployeeId,
                        Collectors.summingDouble(s -> {
                            Member m = memberDAO.findById(s.getMemberId()).orElse(null);
                            boolean isVIP = m != null && "VIP".equalsIgnoreCase(m.getLevel());
                            return s.getTotalPrice(isVIP);
                        })
                ));
        employeeSales.forEach((empId, total) -> {
            Employee e = employeeDAO.findById(empId).orElse(null);
            if(e != null) barDataset.addValue(total, "銷售額", e.getName());
        });
        JFreeChart barChart = ChartFactory.createBarChart(
                "員工銷售額", "員工", "金額", barDataset,
                PlotOrientation.VERTICAL, true, true, false
        );
        Font font = new Font("微軟正黑體", Font.PLAIN, 12);
        barChart.getTitle().setFont(new Font("微軟正黑體", Font.BOLD, 14));
        barChart.getCategoryPlot().getDomainAxis().setLabelFont(font);
        barChart.getCategoryPlot().getDomainAxis().setTickLabelFont(font);
        barChart.getCategoryPlot().getRangeAxis().setLabelFont(font);
        barChartPanel.setChart(barChart);

        // 圓餅圖: 藥品銷售比例
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        Map<Integer, Double> drugSales = sales.stream()
                .collect(Collectors.groupingBy(Sale::getDrugId,
                        Collectors.summingDouble(s -> {
                            Member m = memberDAO.findById(s.getMemberId()).orElse(null);
                            boolean isVIP = m != null && "VIP".equalsIgnoreCase(m.getLevel());
                            return s.getTotalPrice(isVIP);
                        })));
        drugSales.forEach((drugId, total) -> {
            Drug d = drugDAO.findById(drugId);
            if(d != null) pieDataset.setValue(d.getName(), total);
        });
        JFreeChart pieChart = ChartFactory.createPieChart(
                "藥品銷售比例", pieDataset, true, true, false
        );
        pieChart.getTitle().setFont(new Font("微軟正黑體", Font.BOLD, 14));
        pieChart.getLegend().setItemFont(font);
        pieChartPanel.setChart(pieChart);

        // 表格明細
        tableModel.setRowCount(0);
        for(Sale s : sales) {
            Employee e = employeeDAO.findById(s.getEmployeeId()).orElse(null);
            Member m = memberDAO.findById(s.getMemberId()).orElse(null);
            Drug d = drugDAO.findById(s.getDrugId());
            if(e != null && m != null && d != null) {
                boolean isVIP = "VIP".equalsIgnoreCase(m.getLevel());
                tableModel.addRow(new Object[]{
                        e.getName(),
                        m.getName(),
                        d.getName(),
                        s.getQty(),
                        s.getUnitPrice(),
                        s.getTotalPrice(isVIP),
                        s.getCreatedAt()
                });
            }
        }

        revalidate();
        repaint();
    }

    private void exportExcel() {
        try {
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String defaultFileName = "SalesReport_" + timestamp + ".xlsx";

            java.io.File outputDir = new java.io.File("Excel");
            if (!outputDir.exists()) outputDir.mkdirs();
            java.io.File outputFile = new java.io.File(outputDir, defaultFileName);

            try (Workbook wb = new XSSFWorkbook()) {
                Sheet sheet = wb.createSheet("銷售報表");

                // 表格標題
                Row header = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = header.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));
                }

                // 表格資料
                for (int r = 0; r < tableModel.getRowCount(); r++) {
                    Row row = sheet.createRow(r + 1);
                    for (int c = 0; c < tableModel.getColumnCount(); c++) {
                        Cell cell = row.createCell(c);
                        Object val = tableModel.getValueAt(r, c);
                        if (val instanceof Number)
                            cell.setCellValue(((Number) val).doubleValue());
                        else
                            cell.setCellValue(val.toString());
                    }
                }

                // 圖表匯出
                addChartToSheet(wb, sheet, barChartPanel.getChart(), 7, 1);
                addChartToSheet(wb, sheet, pieChartPanel.getChart(), 7, 20);

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    wb.write(fos);
                }

                JOptionPane.showMessageDialog(this, "匯出成功!\n檔案位置: " + outputFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "匯出失敗: " + ex.getMessage());
        }
    }

    private void addChartToSheet(Workbook wb, Sheet sheet, JFreeChart chart, int col, int row) throws Exception {
        BufferedImage img = chart.createBufferedImage(600,300);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);

        int pictureIdx = wb.addPicture(bos.toByteArray(), Workbook.PICTURE_TYPE_PNG);
        bos.close();

        CreationHelper helper = wb.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(col);
        anchor.setRow1(row);
        anchor.setCol2(col+10);
        anchor.setRow2(row+15);

        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize();
    }
}
