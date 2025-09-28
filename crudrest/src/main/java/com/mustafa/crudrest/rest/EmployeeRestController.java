package com.mustafa.crudrest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mustafa.crudrest.dao.EmployeeDAO;
import com.mustafa.crudrest.entity.Employee;
import com.mustafa.crudrest.fileManagement.FileExporter;
import com.mustafa.crudrest.fileManagement.FileResponse;
import com.mustafa.crudrest.service.EmployeeService;
import com.mustafa.crudrest.service.FileExporterService;
import com.mustafa.crudrest.service.JasperReportService;
import com.mustafa.crudrest.fileManagement.JasperExporter;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;
    private final FileExporterService fileExporterService;

    @Autowired
    private JasperExporter jasperExporter;

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, ObjectMapper objectMapper,
                                  FileExporterService fileExporterService) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
        this.fileExporterService = fileExporterService;
    }

    @GetMapping("/export")
    public ResponseEntity<FileResponse> export(@RequestParam String format,
                                               @RequestParam(defaultValue = "employees") String fileName,
                                               @RequestParam(required = false) String reportTitle) throws IOException {
        List<Employee> employees = employeeService.findAll();
        if (employees.isEmpty()) {
            return ResponseEntity.ok(new FileResponse(null, null, null, "No employees found"));
        }

        byte[] fileBytes;
        String fileType;
        String fileExtension;

        // Handle JasperReports formats
        if (format.startsWith("jasper-")) {
            String jasperFormat = format.substring(7); // Remove "jasper-" prefix

            Map<String, Object> customParams = new HashMap<>();
            if (reportTitle != null) {
                customParams.put("Par1", reportTitle);
            } else {
                customParams.put("Par1", "Employee Report");
            }

            fileBytes = jasperExporter.export(employees, jasperFormat, customParams);
            fileType = jasperExporter.getFileType(jasperFormat);
            fileExtension = jasperExporter.getFileExtension(jasperFormat);
        } else {
            // Handle existing formats
            FileExporter exporter = fileExporterService.getExporter(format);
            fileBytes = exporter.export(employees);
            fileType = exporter.getFileType();
            fileExtension = exporter.getFileExtension();
        }

        String base64Content = Base64.getEncoder().encodeToString(fileBytes);

        FileResponse response = new FileResponse(
                fileName + fileExtension,
                fileType,
                base64Content,
                "File exported successfully"
        );

        return ResponseEntity.ok(response);
    }

    // New endpoint for filtered Jasper reports
    @GetMapping("/export/filtered")
    public ResponseEntity<FileResponse> exportFiltered(
            @RequestParam String format,
            @RequestParam(required = false) String firstName,
            @RequestParam(defaultValue = "filtered_employees") String fileName,
            @RequestParam(required = false) String reportTitle) throws IOException {

        List<Employee> employees;

        if (firstName != null && !firstName.trim().isEmpty()) {
            // Use your existing findByFN service method
            employees = employeeService.findByFN(firstName);
        } else {
            employees = employeeService.findAll();
        }

        if (employees.isEmpty()) {
            return ResponseEntity.ok(new FileResponse(null, null, null, "No employees found"));
        }

        // Only support Jasper formats for filtered export
        if (!format.startsWith("jasper-")) {
            format = "jasper-" + format; // Auto-convert to Jasper format
        }

        String jasperFormat = format.substring(7);

        Map<String, Object> customParams = new HashMap<>();
        customParams.put("Par1", reportTitle != null ? reportTitle :
                (firstName != null ? "Employees named " + firstName : "All Employees"));

        byte[] fileBytes = jasperExporter.export(employees, jasperFormat, customParams);
        String fileType = jasperExporter.getFileType(jasperFormat);
        String fileExtension = jasperExporter.getFileExtension(jasperFormat);

        String base64Content = Base64.getEncoder().encodeToString(fileBytes);

        FileResponse response = new FileResponse(
                fileName + fileExtension,
                fileType,
                base64Content,
                "Filtered report exported successfully"
        );

        return ResponseEntity.ok(response);
    }

    // Endpoint to compile Jasper reports
    @PostMapping("/compile-report")
    public ResponseEntity<String> compileReport(@RequestParam String reportName) {
        try {
            jasperReportService.compileReport(reportName);
            return ResponseEntity.ok("Report compiled successfully: " + reportName);
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to compile report: " + e.getMessage());
        }
    }

    // Endpoint to test Jasper report with specific parameters
    @GetMapping("/export/jasper-test")
    public ResponseEntity<FileResponse> testJasperReport(
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(required = false) String title) throws IOException {

        List<Employee> employees = employeeService.findAll();
        if (employees.isEmpty()) {
            return ResponseEntity.ok(new FileResponse(null, null, null, "No employees found"));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Par1", title != null ? title : "Test Employee Report");

        byte[] fileBytes = jasperExporter.export(employees, format, parameters);
        String fileType = jasperExporter.getFileType(format);
        String fileExtension = jasperExporter.getFileExtension(format);

        String base64Content = Base64.getEncoder().encodeToString(fileBytes);

        FileResponse response = new FileResponse(
                "test_report" + fileExtension,
                fileType,
                base64Content,
                "Jasper test report generated successfully"
        );

        return ResponseEntity.ok(response);
    }

    // Existing methods remain unchanged
    @GetMapping("/employees")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    @GetMapping("/employeesByFN/{FN}")
    public List<Employee> findByFN(@PathVariable String FN){
        return employeeService.findByFN(FN);
    }

    @GetMapping("/employees/{id}")
    public Employee findById(@PathVariable int id){
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            throw new RuntimeException("Invalid ID - " + id);
        }
        return employee;
    }

    @PostMapping("/employees")
    public Employee save(@RequestBody Employee employee) {
        employee.setId(0);
        return employeeService.save(employee);
    }

    @PutMapping("/employees")
    public Employee update(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PatchMapping("employees/{id}")
    public Employee patch(@PathVariable int id, @RequestBody Map<String,Object> patchPayLoad) {
        Employee tempEmployee = employeeService.findById(id);
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + id);
        }
        if (patchPayLoad.containsKey("id")) {
            throw new RuntimeException("Employee id not allowed in request body - " + id);
        }
        Employee patchedEmployee = apply(patchPayLoad, tempEmployee);
        return employeeService.save(patchedEmployee);
    }

    @DeleteMapping("employees/{id}")
    public String delete(@PathVariable int id){
        Employee tempEmployee = employeeService.findById(id);
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + id);
        }
        employeeService.deleteById(id);
        return "Deleted employee with id " + id;
    }

    private Employee apply(Map<String, Object> patchPayLoad, Employee tempEmployee) {
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);
        ObjectNode patchNode = objectMapper.convertValue(patchPayLoad, ObjectNode.class);
        employeeNode.setAll(patchNode);
        return objectMapper.convertValue(employeeNode, Employee.class);
    }
}