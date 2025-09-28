package com.mustafa.crudrest.fileManagement;

import com.mustafa.crudrest.entity.Employee;
import com.mustafa.crudrest.service.JasperReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JasperExporter implements FileExporter {

    @Autowired
    private JasperReportService jasperReportService;

    @Override
    public byte[] export(List<Employee> employees) throws IOException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Par1", "Employee List Report");
            parameters.put("REPORT_TITLE", "Employee Data");

            return jasperReportService.generateEmployeeReport("Simple_Blue_1", employees, parameters, "pdf");
        } catch (JRException e) {
            throw new IOException("Failed to generate JasperReport", e);
        }
    }

    // Overloaded method for different formats
    public byte[] export(List<Employee> employees, String format) throws IOException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Par1", "Employee Data Export");
            parameters.put("REPORT_TITLE", "Employee Information");

            return jasperReportService.generateEmployeeReport("Simple_Blue_1", employees, parameters, format);
        } catch (JRException e) {
            throw new IOException("Failed to generate JasperReport", e);
        }
    }

    // Method with custom parameters
    public byte[] export(List<Employee> employees, String format, Map<String, Object> customParams) throws IOException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Par1", "Custom Employee Report");
            parameters.put("REPORT_TITLE", "Employee Details");

            if (customParams != null) {
                parameters.putAll(customParams);
            }

            return jasperReportService.generateEmployeeReport("Simple_Blue_1", employees, parameters, format);
        } catch (JRException e) {
            throw new IOException("Failed to generate JasperReport", e);
        }
    }

    @Override
    public String getFileType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }

    public String getFileType(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "word" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/pdf";
        };
    }

    public String getFileExtension(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> ".xlsx";
            case "word" -> ".docx";
            default -> ".pdf";
        };
    }
}