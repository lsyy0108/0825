package dao.impl;

import dao.MemberDAO;
import model.Member;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDAOImpl implements MemberDAO {

    @Override
    public int create(Member member) {
        String sql = "INSERT INTO members(name, phone, email, level, member_code) VALUES(?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getPhone());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getLevel());
            ps.setString(5, member.getMemberCode());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Member member) {
        String sql = "UPDATE members SET name=?, phone=?, email=?, level=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getPhone());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getLevel());
            ps.setInt(5, member.getId());
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM members WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Member> findAll() {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT id, name, phone, email, level, member_code FROM members";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Member m = new Member(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("level"),
                    rs.getString("member_code")
                );
                list.add(m);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Member> findById(int id) {
        String sql = "SELECT id, name, phone, email, level, member_code FROM members WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Member m = new Member(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("level"),
                    rs.getString("member_code")
                );
                return Optional.of(m);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByMemberCode(String memberCode) {
        String sql = "SELECT id, name, phone, email, level, member_code FROM members WHERE member_code=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberCode);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Member m = new Member(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("level"),
                    rs.getString("member_code")
                );
                return Optional.of(m);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
