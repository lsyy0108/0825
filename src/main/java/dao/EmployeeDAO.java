package dao;

import model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {
    int create(Employee e);
    boolean update(Employee e);
    boolean delete(int id);
    Optional<Employee> findById(int id);
    List<Employee> findAll();
    Optional<Employee> findByUsername(String username);
}
