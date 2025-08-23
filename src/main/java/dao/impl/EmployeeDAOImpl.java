package dao.impl;

import dao.EmployeeDAO;
import model.Employee;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public int create(Employee e) {
        String sql = "INSERT INTO employees (name, role, username, password, employee_code) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getUsername());
            ps.setString(4, e.getPassword());
            ps.setString(5, e.getEmployeeCode());
            int affected = ps.executeUpdate();
            if(affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Employee e) {
        String sql = "UPDATE employees SET name=?, role=?, username=?, password=?, employee_code=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getUsername());
            ps.setString(4, e.getPassword());
            ps.setString(5, e.getEmployeeCode());
            ps.setInt(6, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Employee> findById(int id) {
        String sql = "SELECT * FROM employees WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Employee e = extractEmployee(rs);
                return Optional.of(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                list.add(extractEmployee(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Employee> findByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Employee e = extractEmployee(rs);
                return Optional.of(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setName(rs.getString("name"));
        e.setRole(rs.getString("role"));
        e.setUsername(rs.getString("username"));
        e.setPassword(rs.getString("password"));
        e.setEmployeeCode(rs.getString("employee_code"));
        return e;
    }
}
