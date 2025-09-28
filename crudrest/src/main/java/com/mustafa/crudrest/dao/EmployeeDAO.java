package com.mustafa.crudrest.dao;

import com.mustafa.crudrest.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll();

    Employee findById(int id);

    //insert and update
    Employee save(Employee employee);

    void deleteById(int id);


}
