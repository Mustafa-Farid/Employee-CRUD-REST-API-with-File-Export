package com.mustafa.crudrest.fileManagement;

import com.mustafa.crudrest.entity.Employee;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class PdfExporter implements FileExporter {

    @Override
    public byte[] export(List<Employee> employees) throws IOException {
        PDDocument document = new PDDocument();

        try {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set margins and starting position
            float margin = 50;
            float currentY = page.getMediaBox().getHeight() - margin;

            // Title
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            drawText(contentStream, margin, currentY, "EMPLOYEE LIST");
            currentY -= 30;

            // Headers - using proper spacing instead of tabs
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            drawText(contentStream, margin, currentY, "ID");
            drawText(contentStream, margin + 50, currentY, "NAME");
            drawText(contentStream, margin + 200, currentY, "EMAIL");
            currentY -= 20;

            // Draw line under headers
            contentStream.moveTo(margin, currentY + 5);
            contentStream.lineTo(page.getMediaBox().getWidth() - margin, currentY + 5);
            contentStream.stroke();

            currentY -= 10;

            // Data rows
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            for (Employee employee : employees) {
                if (currentY < margin + 20) {
                    // Create new page if we're at the bottom
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    currentY = page.getMediaBox().getHeight() - margin;
                }

                // Draw employee data with proper column spacing
                drawText(contentStream, margin, currentY, String.valueOf(employee.getId()));
                drawText(contentStream, margin + 50, currentY,
                        employee.getFirstName() + " " + employee.getLastName());
                drawText(contentStream, margin + 200, currentY, employee.getEmail());

                currentY -= 15;
            }

            contentStream.close();

        } catch (IOException e) {
            document.close();
            throw e;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    // Helper method to draw text at specific coordinates
    private void drawText(PDPageContentStream contentStream, float x, float y, String text) throws IOException {
        if (text == null) text = "";
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    @Override
    public String getFileType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }
}