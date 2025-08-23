package controller;

import dao.impl.EmployeeDAOImpl;
import model.Employee;
import util.CodeGeneratorUtil;
import util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EmployeesUI extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private EmployeeDAOImpl dao = new EmployeeDAOImpl();

    public EmployeesUI(Employee loggedInEmployee) {
    	setBackground(new Color(255, 128, 192));
        setTitle("員工管理 - 登入者: " + loggedInEmployee.getName());
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID","員工編號","姓名","職位","帳號","密碼"}, 0){
            @Override
            public boolean isCellEditable(int row, int col){ return false; }
        };
        table = new JTable(tableModel);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(new Color(255, 128, 192));
        btnAdd = new JButton("新增");
        btnEdit = new JButton("修改");
        btnDelete = new JButton("刪除");
        btnRefresh = new JButton("刷新");
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);

        loadTable();
        initActions();
        setRolePermissions(loggedInEmployee);
    }

    private void loadTable(){
        tableModel.setRowCount(0);
        List<Employee> list = dao.findAll();
        if(list != null){
            for(Employee e : list){
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getEmployeeCode(),
                        e.getName(),
                        e.getRole(),
                        e.getUsername(),
                        e.getPassword()
                });
            }
        }
    }

    private void initActions(){
        btnAdd.addActionListener(e -> addEmployee());
        btnEdit.addActionListener(e -> editEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnRefresh.addActionListener(e -> loadTable());
    }

    private void setRolePermissions(Employee loggedInEmployee){
        if("PHARMACIST".equalsIgnoreCase(loggedInEmployee.getRole())){
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

    private void addEmployee(){
        JTextField txtName = new JTextField();
        String[] roles = {"MANAGER", "PHARMACIST"}; // 職位下拉選單
        JComboBox<String> cmbRole = new JComboBox<>(roles);
        JTextField txtUsername = new JTextField();
        JTextField txtPassword = new JTextField();

        Object[] message = {
                "姓名:", txtName,
                "職位:", cmbRole,
                "帳號:", txtUsername,
                "密碼:", txtPassword
        };

        int option = JOptionPane.showConfirmDialog(this, message, "新增員工", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            try(Connection conn = DBUtil.getConnection()){
                String name = txtName.getText().trim();
                String role = cmbRole.getSelectedItem().toString();
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();

                Employee e = new Employee();
                e.setName(name);
                e.setRole(role);
                e.setUsername(username);
                e.setPassword(password);

                // 自動生成員工編號
                String empCode = CodeGeneratorUtil.generateEmployeeCode(conn);
                e.setEmployeeCode(empCode);

                int id = dao.create(e);
                if(id > 0){
                    JOptionPane.showMessageDialog(this,"新增成功");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this,"新增失敗","錯誤",JOptionPane.ERROR_MESSAGE);
                }
            } catch(SQLException ex){
                JOptionPane.showMessageDialog(this,"資料庫錯誤: " + ex.getMessage(),"錯誤",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editEmployee(){
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"請選擇一筆資料"); return; }

        int id = (int) tableModel.getValueAt(row,0);
        Optional<Employee> empOpt = dao.findById(id);
        if(empOpt.isEmpty()){
            JOptionPane.showMessageDialog(this,"找不到資料"); return;
        }
        Employee e = empOpt.get();

        JTextField txtName = new JTextField(e.getName());
        String[] roles = {"MANAGER", "PHARMACIST"};
        JComboBox<String> cmbRole = new JComboBox<>(roles);
        cmbRole.setSelectedItem(e.getRole());
        JTextField txtUsername = new JTextField(e.getUsername());
        JTextField txtPassword = new JTextField(e.getPassword());

        Object[] message = {
                "姓名:", txtName,
                "職位:", cmbRole,
                "帳號:", txtUsername,
                "密碼:", txtPassword
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改員工", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            try{
                e.setName(txtName.getText().trim());
                e.setRole(cmbRole.getSelectedItem().toString());
                e.setUsername(txtUsername.getText().trim());
                e.setPassword(txtPassword.getText().trim());

                if(dao.update(e)){
                    JOptionPane.showMessageDialog(this,"修改成功");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this,"修改失敗","錯誤",JOptionPane.ERROR_MESSAGE);
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"資料格式錯誤","錯誤",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteEmployee(){
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"請選擇一筆資料"); return; }

        int id = (int) tableModel.getValueAt(row,0);
        int confirm = JOptionPane.showConfirmDialog(this,"確定刪除嗎？","刪除確認",JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        if(dao.delete(id)){
            JOptionPane.showMessageDialog(this,"刪除成功");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this,"刪除失敗","錯誤",JOptionPane.ERROR_MESSAGE);
        }
    }
}
