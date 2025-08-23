package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CodeGeneratorUtil {

    private static final String EMPLOYEE_PREFIX = "E";
    private static final String MEMBER_PREFIX = "M";
    private static final String PRODUCT_PREFIX = "P";
    private static final int CODE_LENGTH = 3; // 例如 E001, M001

    // 生成員工編號
    public static String generateEmployeeCode(Connection conn) throws SQLException {
        return generateCode(conn, "employees", "employee_code", EMPLOYEE_PREFIX);
    }

    // 生成會員編號
    public static String generateMemberCode(Connection conn) throws SQLException {
        return generateCode(conn, "members", "member_code", MEMBER_PREFIX);
    }

    // 生成產品編號
    public static String generateProductCode(Connection conn) throws SQLException {
        return generateCode(conn, "drugs", "drug_code", PRODUCT_PREFIX);
    }

    // 核心方法
    private static String generateCode(Connection conn, String tableName, String columnName, String prefix) throws SQLException {
        String sql = "SELECT MAX(" + columnName + ") AS max_code FROM " + tableName;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            String maxCode = null;
            if (rs.next()) maxCode = rs.getString("max_code");

            int nextNumber = 1; // 預設從 1 開始
            if (maxCode != null) {
                // 取數字部分
                String numPart = maxCode.replaceAll("[^0-9]", "");
                if(!numPart.isEmpty()){
                    nextNumber = Integer.parseInt(numPart) + 1;
                }
            }

            return prefix + String.format("%0" + CODE_LENGTH + "d", nextNumber);
        }
    }
    public static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
}
}
