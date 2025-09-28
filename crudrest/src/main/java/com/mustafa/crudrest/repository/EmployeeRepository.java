package com.mustafa.crudrest.repository;

import com.mustafa.crudrest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    @Procedure(procedureName = "GetEmployeeByUsername")
    List<Employee> GetEmployeeByUsername(@Param("input_username") String username);
}
