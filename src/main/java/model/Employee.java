package model;

public class Employee {
    private int id;
    private String name;
    private String role;      // 職位
    private String username;
    private String password;
    private String employeeCode;

    public Employee() {}

    public Employee(int id, String name, String role, String username, String password, String employeeCode) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.employeeCode = employeeCode;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmployeeCode() { return employeeCode; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
}
