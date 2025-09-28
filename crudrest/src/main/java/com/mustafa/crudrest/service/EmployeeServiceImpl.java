package com.mustafa.crudrest.service;

import com.mustafa.crudrest.dao.EmployeeDAO;
import com.mustafa.crudrest.entity.Employee;
import com.mustafa.crudrest.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeDAO employeeDAO;
    private final EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeDAO employeeDAO, EmployeeRepository employeeRepository){
        this.employeeDAO = employeeDAO;
        this.employeeRepository = employeeRepository;
    }
    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(int id) {
        return employeeDAO.findById(id);
    }

    @Override
    @Transactional
    public List<Employee> findByFN(String firstname) {
        return employeeRepository.GetEmployeeByUsername(firstname);
    }

    @Transactional
    @Override
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }
    @Transactional
    @Override
    public void deleteById(int id) {
        employeeDAO.deleteById(id);
    }
}
