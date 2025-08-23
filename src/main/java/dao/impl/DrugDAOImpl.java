package dao.impl;

import dao.DrugDAO;
import model.Drug;
import util.DBUtil;
import util.CodeGeneratorUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrugDAOImpl implements DrugDAO {

    @Override
    public int create(Drug drug) {
        String sql = "INSERT INTO drugs (name, category, unit, stock, price, expiration_date, drug_code) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection()) {
            drug.setDrugCode(CodeGeneratorUtil.generateProductCode(conn));
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, drug.getName());
            ps.setString(2, drug.getCategory());
            ps.setString(3, drug.getUnit());
            ps.setInt(4, drug.getStock());
            ps.setBigDecimal(5, drug.getPrice());
            ps.setDate(6, Date.valueOf(drug.getExpirationDate()));
            ps.setString(7, drug.getDrugCode());

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
    public boolean update(Drug drug) {
        String sql = "UPDATE drugs SET name=?, category=?, unit=?, stock=?, price=?, expiration_date=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, drug.getName());
            ps.setString(2, drug.getCategory());
            ps.setString(3, drug.getUnit());
            ps.setInt(4, drug.getStock());
            ps.setBigDecimal(5, drug.getPrice());
            ps.setDate(6, Date.valueOf(drug.getExpirationDate()));
            ps.setInt(7, drug.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM drugs WHERE id=?";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Drug findById(int id) {
        String sql = "SELECT * FROM drugs WHERE id=?";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Drug> findAll() {
        List<Drug> list = new ArrayList<>();
        String sql = "SELECT * FROM drugs";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Drug mapRow(ResultSet rs) throws SQLException {
        Drug d = new Drug();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setCategory(rs.getString("category"));
        d.setUnit(rs.getString("unit"));
        d.setStock(rs.getInt("stock"));
        d.setPrice(rs.getBigDecimal("price"));
        d.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
        d.setDrugCode(rs.getString("drug_code"));
        return d;
    }
}
