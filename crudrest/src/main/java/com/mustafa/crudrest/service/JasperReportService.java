package com.mustafa.crudrest.service;

import com.mustafa.crudrest.entity.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    // Remove database connection dependencies since we're using EmployeeService
    // @Autowired
    // private DataSource dataSource;
    //
    // @Autowired
    // private JdbcTemplate jdbcTemplate;

    // Method for Java collection data sources (using your EmployeeService)
    public byte[] generateReportFromBeans(String reportName, List<?> data,
                                          Map<String, Object> parameters, String format)
            throws JRException {

        if (parameters == null) {
            parameters = new HashMap<>();
        }

        // 1. Load the compiled report
        JasperReport jasperReport = loadCompiledReport(reportName);

        // 2. Create data source from Java beans (your Employee entities)
        JRDataSource dataSource = new JRBeanCollectionDataSource(data);

        // 3. Fill the report with your Java data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 4. Export based on format
        return exportReport(jasperPrint, format);
    }

    // Method specifically for Employee reports
    public byte[] generateEmployeeReport(String reportName, List<Employee> employees,
                                         Map<String, Object> parameters, String format)
            throws JRException {

        if (parameters == null) {
            parameters = new HashMap<>();
        }

        // Add default parameters if not provided
        parameters.putIfAbsent("REPORT_TITLE", "Employee Report");
        parameters.putIfAbsent("GENERATED_BY", "Employee Management System");

        return generateReportFromBeans(reportName, employees, parameters, format);
    }

    private JasperReport loadCompiledReport(String reportName) throws JRException {
        String reportPath = "reports/" + reportName + ".jasper";
        try (InputStream inputStream = new ClassPathResource(reportPath).getInputStream()) {
            return (JasperReport) JRLoader.loadObject(inputStream);
        } catch (Exception e) {
            throw new JRException("Failed to load report: " + reportName, e);
        }
    }

    private byte[] exportReport(JasperPrint jasperPrint, String format) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        switch (format.toLowerCase()) {
            case "pdf":
                JRPdfExporter pdfExporter = new JRPdfExporter();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                pdfExporter.exportReport();
                break;

            case "excel":
                JRXlsxExporter excelExporter = new JRXlsxExporter();
                excelExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                excelExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                excelExporter.exportReport();
                break;

            case "word":
                JRDocxExporter wordExporter = new JRDocxExporter();
                wordExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                wordExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                wordExporter.exportReport();
                break;

            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }

        return outputStream.toByteArray();
    }

    // Method to compile JRXML files
    public void compileReport(String jrxmlFileName) throws JRException {
        String jrxmlPath = "reports/" + jrxmlFileName + ".jrxml";
        String jasperPath = "src/main/resources/reports/" + jrxmlFileName + ".jasper";

        try (InputStream inputStream = new ClassPathResource(jrxmlPath).getInputStream()) {
            JasperCompileManager.compileReportToStream(inputStream,
                    new java.io.FileOutputStream(jasperPath));
        } catch (Exception e) {
            throw new JRException("Failed to compile report: " + jrxmlFileName, e);
        }
    }
}