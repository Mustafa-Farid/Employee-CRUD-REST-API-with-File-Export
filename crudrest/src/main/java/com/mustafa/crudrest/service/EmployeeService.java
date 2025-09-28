package com.mustafa.crudrest.service;

import com.mustafa.crudrest.entity.Employee;
import jakarta.transaction.Transactional;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int id);

    List<Employee> findByFN(String firstname);

    //insert and update
    Employee save(Employee employee);

    void deleteById(int id);
}
