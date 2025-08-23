package controller;

import dao.impl.DrugDAOImpl;
import dao.impl.MemberDAOImpl;
import dao.impl.SalesDAOImpl;
import model.Drug;
import model.Employee;
import model.Member;
import model.Sale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.File;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class SalesUI extends JFrame {
	private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Member> cmbMember;
    private JComboBox<Drug> cmbDrug;
    private JTextField txtQty;
    private JButton btnAddSale, btnRefresh, btnDelete, btnUpdate;
    private JButton btnQuery_1;

    private SalesDAOImpl salesDAO = new SalesDAOImpl();
    private MemberDAOImpl memberDAO = new MemberDAOImpl();
    private DrugDAOImpl drugDAO = new DrugDAOImpl();
    private Employee loggedInEmployee;

    private JTextArea textArea;          // 暫存訂單顯示
    private JTextArea textArea_1;        // 查詢結果顯示
    private List<Sale> tempSales = new ArrayList<>();
    private JTextField textField;        // 查詢訂單號

    public SalesUI(Employee loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;

        getContentPane().setBackground(new Color(255, 128, 192));
        setBackground(new Color(255, 128, 192));
        setTitle("銷售作業 - 登入者: " + loggedInEmployee.getName());
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        initTopPanel();
        initTable();
        initTextAreas();
        initButtons();

        loadTable();
        initActions();
    }

    private void initTopPanel() {
        JPanel panelTop = new JPanel();
        panelTop.setBounds(0, 0, 986, 33);
        panelTop.setBackground(new Color(255, 128, 192));

        cmbMember = new JComboBox<>(memberDAO.findAll().toArray(new Member[0]));
        cmbDrug = new JComboBox<>(drugDAO.findAll().toArray(new Drug[0]));
        txtQty = new JTextField(5);
        btnAddSale = new JButton("新增");
        btnUpdate = new JButton("修改");
        btnDelete = new JButton("刪除");
        btnRefresh = new JButton("刷新");

        panelTop.add(new JLabel("會員:")); panelTop.add(cmbMember);
        panelTop.add(new JLabel("藥品:")); panelTop.add(cmbDrug);
        panelTop.add(new JLabel("數量:")); panelTop.add(txtQty);
        panelTop.add(btnAddSale); panelTop.add(btnUpdate);
        panelTop.add(btnDelete); panelTop.add(btnRefresh);

        getContentPane().add(panelTop);
    }

    private void initTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"訂單","會員","藥品","數量","單價","總價","時間"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 35, 996, 269);
        getContentPane().add(scrollPane);
    }

    private void initTextAreas() {
        textArea = new JTextArea();
        textArea.setBounds(377, 303, 490, 160);
        getContentPane().add(textArea);

        textArea_1 = new JTextArea();
        textArea_1.setBounds(0, 352, 284, 111);
        getContentPane().add(textArea_1);
    }

    private void initButtons() {
        JButton btnConfirm = new JButton("送出訂單");
        btnConfirm.setBounds(877, 314, 99, 149);
        btnConfirm.addActionListener(e -> confirmSave());
        getContentPane().add(btnConfirm);

        JLabel label = new JLabel("訂單號碼:");
        label.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        label.setBounds(10, 314, 74, 33);
        getContentPane().add(label);

        textField = new JTextField();
        textField.setBounds(81, 314, 203, 28);
        textField.setColumns(10);
        getContentPane().add(textField);

        JButton btnQuery = new JButton("查詢訂單");
        btnQuery.setFont(new Font("新細明體", Font.PLAIN, 12));
        btnQuery.setBounds(287, 314, 87, 69);
        btnQuery.addActionListener(e -> queryOrder());
        getContentPane().add(btnQuery);

        btnQuery_1 = new JButton("匯出");
        btnQuery_1.setFont(new Font("新細明體", Font.PLAIN, 12));
        btnQuery_1.setBounds(287, 389, 87, 69);
        btnQuery_1.addActionListener(e -> exportToWord());
        getContentPane().add(btnQuery_1);
    }

    private void initActions() {
        btnAddSale.addActionListener(e -> tempSave());
        btnRefresh.addActionListener(e -> { refreshDropdowns(); loadTable(); });
        btnDelete.addActionListener(e -> deleteSale());
        btnUpdate.addActionListener(e -> updateSale());
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Sale> list = salesDAO.findAll();
        for (Sale s : list) {
            Member m = memberDAO.findById(s.getMemberId()).orElse(null);
            Drug d = drugDAO.findById(s.getDrugId());
            if (m != null && d != null) {
                double total = s.getQty() * s.getUnitPrice();
                if ("VIP".equalsIgnoreCase(m.getLevel())) total *= 0.8;
                tableModel.addRow(new Object[]{
                        s.getOrderId(),
                        m.getName(),
                        d.getName(),
                        s.getQty(),
                        s.getUnitPrice(),
                        total,
                        s.getCreatedAt()
                });
            }
        }
    }

    private void tempSave() {
        try {
            Member m = (Member) cmbMember.getSelectedItem();
            Drug d = (Drug) cmbDrug.getSelectedItem();
            int qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) { JOptionPane.showMessageDialog(this,"數量必須大於0"); return; }

            double total = qty * d.getPrice().doubleValue();
            if ("VIP".equalsIgnoreCase(m.getLevel())) total *= 0.8;

            Sale s = new Sale();
            s.setMemberId(m.getId());
            s.setEmployeeId(loggedInEmployee.getId());
            s.setDrugId(d.getId());
            s.setQty(qty);
            s.setUnitPrice(d.getPrice().doubleValue());
            s.setTotal(total);
            s.setCreatedAt(LocalDateTime.now());

            tempSales.add(s);

            textArea.append("會員: " + m.getName()
                    + " | 藥品: " + d.getName()
                    + " | 數量: " + qty
                    + " | 單價: " + d.getPrice()
                    + " | 總價: " + total
                    + " | 時間: " + s.getCreatedAt() + "\n");

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"資料錯誤","錯誤",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void confirmSave() {
        if (tempSales.isEmpty()) {
            JOptionPane.showMessageDialog(this,"沒有資料");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "確定要將 " + tempSales.size() + " 筆資料寫入資料庫嗎？",
                "確認", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        String orderId = generateOrderId();
        

        for (Sale s : tempSales) {
            Drug d = drugDAO.findById(s.getDrugId());
            if (s.getQty() > d.getStock()) {
                JOptionPane.showMessageDialog(this,"藥品 " + d.getName() + " 庫存不足，無法寫入");
                return;
            }
        }

        for (Sale s : tempSales) {
            s.setOrderId(orderId);
            int id = salesDAO.create(s);
            if (id > 0) {
                Drug d = drugDAO.findById(s.getDrugId());
                d.setStock(d.getStock() - s.getQty());
                drugDAO.update(d);
            }
        }

        JOptionPane.showMessageDialog(this,"資料已寫入資料庫");
        tempSales.clear();
        textArea.append("==== 已確認，資料寫入完成 ====\n");
        loadTable();
    }

    private void deleteSale() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "請選擇要刪除的資料");
            return;
        }

        String orderId = (String) tableModel.getValueAt(row, 0);
        List<Sale> sales = salesDAO.findByOrderId(orderId);
        boolean success = true;

        for (Sale s : sales) {
            if (!salesDAO.delete(s.getId())) {
                success = false;
            } else {
                Drug d = drugDAO.findById(s.getDrugId());
                d.setStock(d.getStock() + s.getQty());
                drugDAO.update(d);
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "刪除成功");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "刪除失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSale() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "請選擇要修改的資料");
            return;
        }

        String orderId = (String) tableModel.getValueAt(row, 0);
        List<Sale> sales = salesDAO.findByOrderId(orderId);
        if (sales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "找不到資料");
            return;
        }

        try {
            Member m = (Member) cmbMember.getSelectedItem();
            Drug d = (Drug) cmbDrug.getSelectedItem();
            int qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0 || qty > d.getStock()) {
                JOptionPane.showMessageDialog(this, "數量不正確或庫存不足");
                return;
            }

            Sale s = sales.get(0);

            Drug oldDrug = drugDAO.findById(s.getDrugId());
            oldDrug.setStock(oldDrug.getStock() + s.getQty());
            drugDAO.update(oldDrug);

            s.setMemberId(m.getId());
            s.setDrugId(d.getId());
            s.setQty(qty);
            s.setUnitPrice(d.getPrice().doubleValue());
            double total = qty * d.getPrice().doubleValue();
            if ("VIP".equalsIgnoreCase(m.getLevel())) total *= 0.8;
            s.setTotal(total);

            if (salesDAO.update(s)) {
                d.setStock(d.getStock() - qty);
                drugDAO.update(d);
                JOptionPane.showMessageDialog(this, "修改成功");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, "修改失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "資料錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void refreshDropdowns() {
        cmbMember.setModel(new DefaultComboBoxModel<>(memberDAO.findAll().toArray(new Member[0])));
        cmbDrug.setModel(new DefaultComboBoxModel<>(drugDAO.findAll().toArray(new Drug[0])));
    }

    private String generateOrderId() {
    	String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long timestamp = System.currentTimeMillis() % 1000; 
        return "ORD" + date + String.format("%06d", timestamp);
    }

    private void queryOrder() {
        String keyword = textField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請輸入訂單號碼");
            return;
        }

        List<Sale> allSales = salesDAO.findAll();
        List<Sale> filteredSales = new ArrayList<>();
        for (Sale s : allSales) {
            if (s.getOrderId().contains(keyword)) filteredSales.add(s);
        }

        textArea_1.setText("");
        if (filteredSales.isEmpty()) {
            textArea_1.setText("查無此訂單");
            return;
        }

        for (Sale s : filteredSales) {
            Member m = memberDAO.findById(s.getMemberId()).orElse(null);
            Drug d = drugDAO.findById(s.getDrugId());
            if (m != null && d != null) {
                double total = s.getQty() * s.getUnitPrice();
                if ("VIP".equalsIgnoreCase(m.getLevel())) total *= 0.8;
                textArea_1.append("會員: " + m.getName()
                        + " | 藥品: " + d.getName()
                        + " | 數量: " + s.getQty()
                        + " | 單價: " + s.getUnitPrice()
                        + " | 總價: " + total
                        + " | 時間: " + s.getCreatedAt() + "\n");
            }
        }
    }

    // 匯出 Word
    private void exportToWord() {
        String content = textArea_1.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "沒有查詢結果可以匯出");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String fileName = "WORD/" + LocalDateTime.now().format(formatter) + ".docx";

            File dir = new File("WORD");
            if (!dir.exists()) dir.mkdirs();

            try (XWPFDocument doc = new XWPFDocument()) {
                for (String line : content.split("\n")) {
                    XWPFParagraph p = doc.createParagraph();
                    XWPFRun run = p.createRun();
                    run.setText(line);
                }
                try (FileOutputStream out = new FileOutputStream(fileName)) {
                    doc.write(out);
                }
            }

            JOptionPane.showMessageDialog(this, "已匯出 Word 檔案至 " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "匯出失敗: " + e.getMessage());
        }
    }
}
