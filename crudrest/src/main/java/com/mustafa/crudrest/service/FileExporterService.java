package com.mustafa.crudrest.service;

import com.mustafa.crudrest.fileManagement.ExcelExporter;
import com.mustafa.crudrest.fileManagement.FileExporter;
import com.mustafa.crudrest.fileManagement.PdfExporter;
import com.mustafa.crudrest.fileManagement.WordExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileExporterService {
    @Autowired
    private ExcelExporter excelExporter;

    @Autowired
    private PdfExporter pdfExporter;

    @Autowired
    private WordExporter wordExporter;

    public FileExporter getExporter(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> excelExporter;
            case "pdf" -> pdfExporter;
            case "word" -> wordExporter;
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }
}
