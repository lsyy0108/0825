package service;

import model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    int createEmployee(Employee e);
    boolean updateEmployee(Employee e);
    boolean deleteEmployee(int id);
    Optional<Employee> getEmployeeById(int id);
    List<Employee> getAllEmployees();
    Employee getEmployeeByUsername(String username);
}
