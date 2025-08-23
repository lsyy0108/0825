package controller;

import model.Drug;
import model.Employee;
import service.DrugService;
import service.impl.DrugServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DrugsUI extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private DrugService drugService = new DrugServiceImpl();
    private Employee loggedInEmployee;

    public DrugsUI(Employee loggedInEmployee) {
    	getContentPane().setBackground(new Color(255, 128, 192));
    	setBackground(new Color(255, 128, 192));
        this.loggedInEmployee = loggedInEmployee;

        setTitle("藥品管理 - 登入者: " + loggedInEmployee.getName());
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID","編號","名稱","類別","單位","庫存","價格","效期"},0);
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
        setRolePermissions();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Drug> list = drugService.getAllDrugs();
        for(Drug d : list) {
            tableModel.addRow(new Object[]{
                    d.getId(),
                    d.getDrugCode(),
                    d.getName(),
                    d.getCategory(),
                    d.getUnit(),
                    d.getStock(),
                    d.getPrice().setScale(0, BigDecimal.ROUND_HALF_UP),
                    d.getExpirationDate()
            });
        }
    }

    private void initActions() {
        btnAdd.addActionListener(e -> addDrug());
        btnEdit.addActionListener(e -> editDrug());
        btnDelete.addActionListener(e -> deleteDrug());
        btnRefresh.addActionListener(e -> loadTable());
    }

    private void setRolePermissions() {
        if("PHARMACIST".equalsIgnoreCase(loggedInEmployee.getRole())) {
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

    private void addDrug() {
        JTextField txtName = new JTextField();
        JTextField txtCategory = new JTextField();
        JTextField txtUnit = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtExpiration = new JTextField();

        Object[] message = {
                "名稱:", txtName,
                "類別:", txtCategory,
                "單位:", txtUnit,
                "庫存:", txtStock,
                "價格:", txtPrice,
                "效期(yyyy-mm-dd):", txtExpiration
        };

        int option = JOptionPane.showConfirmDialog(this, message, "新增藥品", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            try{
                Drug d = new Drug();
                d.setName(txtName.getText().trim());
                d.setCategory(txtCategory.getText().trim());
                d.setUnit(txtUnit.getText().trim());
                d.setStock(Integer.parseInt(txtStock.getText().trim()));
                d.setPrice(new BigDecimal(txtPrice.getText().trim()));
                d.setExpirationDate(LocalDate.parse(txtExpiration.getText().trim()));

                int id = drugService.createDrug(d);
                if(id > 0){
                    JOptionPane.showMessageDialog(this,"新增成功");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this,"新增失敗","錯誤",JOptionPane.ERROR_MESSAGE);
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"資料格式錯誤","錯誤",JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void editDrug() {
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"請選擇一筆資料"); return; }
        int id = (int) tableModel.getValueAt(row,0);
        Drug d = drugService.getDrugById(id);
        if(d==null){ JOptionPane.showMessageDialog(this,"找不到資料"); return; }

        JTextField txtName = new JTextField(d.getName());
        JTextField txtCategory = new JTextField(d.getCategory());
        JTextField txtUnit = new JTextField(d.getUnit());
        JTextField txtStock = new JTextField(String.valueOf(d.getStock()));
        JTextField txtPrice = new JTextField(String.valueOf(d.getPrice()));
        JTextField txtExpiration = new JTextField(d.getExpirationDate().toString());

        Object[] message = {
                "名稱:", txtName,
                "類別:", txtCategory,
                "單位:", txtUnit,
                "庫存:", txtStock,
                "價格:", txtPrice,
                "效期(yyyy-mm-dd):", txtExpiration
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改藥品", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            try{
                d.setName(txtName.getText().trim());
                d.setCategory(txtCategory.getText().trim());
                d.setUnit(txtUnit.getText().trim());
                d.setStock(Integer.parseInt(txtStock.getText().trim()));
                d.setPrice(new BigDecimal(txtPrice.getText().trim()));
                d.setExpirationDate(LocalDate.parse(txtExpiration.getText().trim()));

                if(drugService.updateDrug(d)){
                    JOptionPane.showMessageDialog(this,"修改成功");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this,"修改失敗","錯誤",JOptionPane.ERROR_MESSAGE);
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"資料格式錯誤","錯誤",JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void deleteDrug() {
        int row = table.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"請選擇一筆資料"); return; }
        int id = (int) tableModel.getValueAt(row,0);
        int confirm = JOptionPane.showConfirmDialog(this,"確定刪除嗎？","刪除確認",JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        if(drugService.deleteDrug(id)){
            JOptionPane.showMessageDialog(this,"刪除成功");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this,"刪除失敗","錯誤",JOptionPane.ERROR_MESSAGE);
        }
    }
}
