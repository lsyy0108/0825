package controller;

import dao.impl.MemberDAOImpl;
import model.Member;
import util.CodeGeneratorUtil;
import util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MembersUI extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private MemberDAOImpl dao = new MemberDAOImpl();

    public MembersUI() {
    	setBackground(new Color(255, 128, 192));
        setTitle("會員管理");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "會員編號", "姓名", "電話", "Email", "等級"}, 0);
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
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Member> list = dao.findAll();
        for (Member m : list) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getMemberCode(),
                    m.getName(),
                    m.getPhone(),
                    m.getEmail(),
                    m.getLevel()
            });
        }
    }

    private void initActions() {
        btnAdd.addActionListener(e -> addMember());
        btnEdit.addActionListener(e -> editMember());
        btnDelete.addActionListener(e -> deleteMember());
        btnRefresh.addActionListener(e -> loadTable());
    }

    private void addMember() {
        JTextField txtName = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        String[] levels = {"一般", "VIP"};
        JComboBox<String> cmbLevel = new JComboBox<>(levels);

        Object[] message = {
                "姓名:", txtName,
                "電話:", txtPhone,
                "Email:", txtEmail,
                "等級:", cmbLevel
        };

        int option = JOptionPane.showConfirmDialog(this, message, "新增會員", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Member m = new Member();
                m.setName(txtName.getText().trim());
                m.setPhone(txtPhone.getText().trim());
                m.setEmail(txtEmail.getText().trim());
                m.setLevel(cmbLevel.getSelectedItem().toString());

                // 自動生成會員編號
                try (Connection conn = DBUtil.getConnection()) {
                    m.setMemberCode(CodeGeneratorUtil.generateMemberCode(conn));
                }

                int id = dao.create(m);
                if (id > 0) {
                    JOptionPane.showMessageDialog(this, "新增成功");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "新增失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "資料格式錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void editMember() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "請選擇一筆資料"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Member m = dao.findById(id).orElse(null);
        if (m == null) { JOptionPane.showMessageDialog(this, "找不到資料"); return; }

        JTextField txtName = new JTextField(m.getName());
        JTextField txtPhone = new JTextField(m.getPhone());
        JTextField txtEmail = new JTextField(m.getEmail());
        String[] levels = {"一般", "VIP"};
        JComboBox<String> cmbLevel = new JComboBox<>(levels);
        cmbLevel.setSelectedItem(m.getLevel());

        Object[] message = {
                "姓名:", txtName,
                "電話:", txtPhone,
                "Email:", txtEmail,
                "等級:", cmbLevel
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改會員", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                m.setName(txtName.getText().trim());
                m.setPhone(txtPhone.getText().trim());
                m.setEmail(txtEmail.getText().trim());
                m.setLevel(cmbLevel.getSelectedItem().toString());

                if (dao.update(m)) {
                    JOptionPane.showMessageDialog(this, "修改成功");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "修改失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "資料格式錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void deleteMember() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "請選擇一筆資料"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "確定刪除嗎？", "刪除確認", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (dao.delete(id)) {
            JOptionPane.showMessageDialog(this, "刪除成功");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "刪除失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
}
