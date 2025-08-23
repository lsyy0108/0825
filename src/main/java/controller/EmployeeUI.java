package controller;

import model.Employee;
import util.CodeGeneratorUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class EmployeeUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton btnManageEmployees, btnManageMembers, btnManageDrugs, btnSales, btnReports;
    private Employee employee;
    private JLabel lblFacebook, lblMap, lblTime;
    private JLabel lblNewLabel;

    public EmployeeUI(Employee employee) {
    	getContentPane().setBackground(new Color(255, 128, 192));
        try {
            this.employee = employee;

            setTitle("藥局管理系統 - " + (employee != null && employee.getRole() != null ? employee.getRole() : ""));
            setSize(600, 350);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // ------------------ 按鈕 ------------------
            btnManageEmployees = new JButton("員工管理");
            btnManageEmployees.setBounds(10, 5, 151, 56);
            btnManageMembers = new JButton("會員管理");
            btnManageMembers.setBounds(10, 71, 151, 56);
            btnManageDrugs = new JButton("藥品管理");
            btnManageDrugs.setBounds(10, 137, 151, 56);
            btnSales = new JButton("銷售作業");
            btnSales.setBounds(10, 203, 151, 56);
            btnReports = new JButton("銷售報表");
            btnReports.setBounds(425, 5, 151, 56);
            getContentPane().setLayout(null);

            getContentPane().add(btnManageEmployees);
            getContentPane().add(btnManageMembers);
            getContentPane().add(btnManageDrugs);
            getContentPane().add(btnSales);
            getContentPane().add(btnReports);

           
            // ------------------ FACEBOOK ------------------
            lblFacebook = new JLabel("<html><a href=''>FACEBOOK</a></html>");
            lblFacebook.setBounds(425, 72, 151, 55);
            lblFacebook.setFont(new Font("微軟正黑體", Font.BOLD, 26));
            lblFacebook.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblFacebook.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openLink("https://reurl.cc/yAD59l");
                }
            });
            getContentPane().add(lblFacebook);

            // ------------------ 地圖 ------------------
            lblMap = new JLabel("<html><a href=''>地圖</a></html>");
            lblMap.setBounds(425, 138, 151, 55);
            lblMap.setFont(new Font("微軟正黑體", Font.BOLD, 26));
            lblMap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblMap.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openLink("https://reurl.cc/896rxb");
                }
            });
            getContentPane().add(lblMap);

            // ------------------ 時間 ------------------
            lblTime = new JLabel("");
            lblTime.setBounds(394, 247, 182, 56);
            lblTime.setFont(new Font("微軟正黑體", Font.BOLD, 16));
            getContentPane().add(lblTime);

            Timer timer = new Timer(1000, e -> lblTime.setText(CodeGeneratorUtil.getCurrentTime()));
            
            lblNewLabel = new JLabel("樂恬診所");
            lblNewLabel.setFont(new Font("微軟正黑體", Font.BOLD, 45));
            lblNewLabel.setBounds(202, 5, 213, 56);
            getContentPane().add(lblNewLabel);
            timer.start();

            // ------------------ 角色權限 ------------------
            setRolePermissions();

            // ------------------ 按鈕事件 ------------------
            initActions();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "開啟主畫面失敗: " + ex.getMessage());
        }
    }

    private void openLink(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "開啟連結失敗: " + ex.getMessage());
            }
        }
    }

    private void setRolePermissions() {
        if (employee != null && "PHARMACIST".equalsIgnoreCase(employee.getRole())) {
            btnManageEmployees.setEnabled(false); // 藥師不能管理員工
            btnReports.setEnabled(true);           // 藥師只看自己的報表
        }
    }

    private void initActions() {
        btnManageEmployees.addActionListener(e -> new EmployeesUI(employee).setVisible(true));
        btnManageMembers.addActionListener(e -> new MembersUI().setVisible(true));
        btnManageDrugs.addActionListener(e -> new DrugsUI(employee).setVisible(true));
        btnSales.addActionListener(e -> new SalesUI(employee).setVisible(true));
        btnReports.addActionListener(e -> {
            JFrame frame = new JFrame("銷售報表");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new SalesReportUI(employee));
            frame.setVisible(true);
        });
    }
}
