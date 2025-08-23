package service.impl;

import dao.EmployeeDAO;
import dao.impl.EmployeeDAOImpl;
import model.Employee;
import service.EmployeeService;

import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private EmployeeDAO dao = new EmployeeDAOImpl();
    @Override
    public int createEmployee(Employee e) {
        
        return employeeDAO.create(e);
    }

    @Override
    public boolean updateEmployee(Employee e) {
        return employeeDAO.update(e);
    }

    @Override
    public boolean deleteEmployee(int id) {
        return employeeDAO.delete(id);
    }

    @Override
    public Optional<Employee> getEmployeeById(int id) {
        return employeeDAO.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return dao.findByUsername(username).orElse(null);
    }
}
