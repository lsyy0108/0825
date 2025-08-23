package controller;

import model.Employee;
import service.EmployeeService;
import service.impl.EmployeeServiceImpl;

import javax.swing.*;
import java.awt.*;

public class EmployeeLogin extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    private EmployeeService employeeService = new EmployeeServiceImpl();

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeLogin frame = new EmployeeLogin();
            frame.setVisible(true);
        });
    }

    public EmployeeLogin() {
        getContentPane().setBackground(new Color(255, 128, 192));
        setBackground(new Color(255, 128, 192));
        setTitle("員工登入");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(3, 2, 5, 5));

        JLabel label = new JLabel("帳號:");
        label.setFont(new Font("微軟正黑體", Font.BOLD, 30));
        getContentPane().add(label);
        txtUsername = new JTextField();
        getContentPane().add(txtUsername);

        JLabel label_1 = new JLabel("密碼:");
        label_1.setFont(new Font("微軟正黑體", Font.BOLD, 30));
        getContentPane().add(label_1);
        txtPassword = new JPasswordField();
        getContentPane().add(txtPassword);

        btnLogin = new JButton("登入");
        btnLogin.setFont(new Font("微軟正黑體", Font.BOLD, 30));
        getContentPane().add(new JLabel()); // 空白佔位
        getContentPane().add(btnLogin);

        // 🔹 綁定 ActionListener（支援按下按鈕 or Enter 鍵）
        btnLogin.addActionListener(e -> loginAction());
        getRootPane().setDefaultButton(btnLogin); // 讓 Enter 也能觸發登入
    }

    /** 登入動作 */
    private void loginAction() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        Employee emp = employeeService.getEmployeeByUsername(username);
        if (emp != null) {
            if (emp.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(this, "登入成功！");
                this.dispose(); // 關閉登入框
                try {
                    EmployeeUI ui = new EmployeeUI(emp);
                    ui.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "開啟主畫面失敗：" + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "密碼錯誤！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "帳號不存在！");
        }
    }
}