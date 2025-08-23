package dao.impl;

import dao.SaleDAO;
import model.Sale;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesDAOImpl implements SaleDAO {

    @Override
    public int create(Sale sale) {
        String sql = "INSERT INTO sales (member_id, employee_id, drug_id, qty, unit_price, total, created_at, order_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sale.getMemberId());
            ps.setInt(2, sale.getEmployeeId());
            ps.setInt(3, sale.getDrugId());
            ps.setInt(4, sale.getQty());
            ps.setDouble(5, sale.getUnitPrice());
            ps.setDouble(6, sale.getTotal());
            ps.setTimestamp(7, Timestamp.valueOf(sale.getCreatedAt()));
            ps.setString(8, sale.getOrderId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean update(Sale sale) {
        String sql = "UPDATE sales SET member_id=?, employee_id=?, drug_id=?, qty=?, unit_price=?, total=?, created_at=?, order_id=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sale.getMemberId());
            ps.setInt(2, sale.getEmployeeId());
            ps.setInt(3, sale.getDrugId());
            ps.setInt(4, sale.getQty());
            ps.setDouble(5, sale.getUnitPrice());
            ps.setDouble(6, sale.getTotal());
            ps.setTimestamp(7, Timestamp.valueOf(sale.getCreatedAt()));
            ps.setString(8, sale.getOrderId());
            ps.setInt(9, sale.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM sales WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Sale> findAll() {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT * FROM sales";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Sale> findByMemberId(int memberId) {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE member_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Sale> findById(int id) {
        String sql = "SELECT * FROM sales WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public List<Sale> findByOrderId(String orderId) {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE order_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Sale mapRow(ResultSet rs) throws SQLException {
        Sale s = new Sale();
        s.setId(rs.getInt("id"));
        s.setMemberId(rs.getInt("member_id"));
        s.setEmployeeId(rs.getInt("employee_id"));
        s.setDrugId(rs.getInt("drug_id"));
        s.setQty(rs.getInt("qty"));
        s.setUnitPrice(rs.getBigDecimal("unit_price").doubleValue());
        s.setTotal(rs.getBigDecimal("total").doubleValue());
        s.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        s.setOrderId(rs.getString("order_id"));
        return s;
    }
    @Override
    public boolean deleteByOrderId(String orderId) {
        String sql = "DELETE FROM sales WHERE order_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
