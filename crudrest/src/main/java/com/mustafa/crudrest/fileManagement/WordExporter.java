package com.mustafa.crudrest.fileManagement;

import com.mustafa.crudrest.entity.Employee;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
@Component
public class WordExporter implements FileExporter {
    @Override
    public byte[] export(List<Employee> employees) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // Create title
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Customer List");
        titleRun.setBold(true);
        titleRun.setFontSize(16);

        // Create table
        XWPFTable table = document.createTable();

        // Create header row
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("ID");
        headerRow.addNewTableCell().setText("First Name");
        headerRow.addNewTableCell().setText("Last Name");
        headerRow.addNewTableCell().setText("Email");
        headerRow.addNewTableCell().setText("Phone");
        headerRow.addNewTableCell().setText("Address");

        // Add data rows
        for (Employee employee : employees) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(employee.getId()));
            row.getCell(1).setText(employee.getFirstName());
            row.getCell(2).setText(employee.getLastName());
            row.getCell(3).setText(employee.getEmail());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    @Override
    public String getFileType() {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    @Override
    public String getFileExtension() {
        return ".docx";
    }
}
