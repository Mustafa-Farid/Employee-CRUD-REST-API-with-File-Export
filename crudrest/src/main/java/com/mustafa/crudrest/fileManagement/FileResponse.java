package com.mustafa.crudrest.fileManagement;

public class FileResponse {
    private String fileName;
    private String fileType;
    private String base64Content;
    private String message;

    // Constructors
    public FileResponse() {}

    public FileResponse(String fileName, String fileType, String base64Content, String message) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.base64Content = base64Content;
        this.message = message;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // toString method (optional)
    @Override
    public String toString() {
        return "FileResponse{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", base64Content='" + (base64Content != null ? base64Content.substring(0, Math.min(50, base64Content.length())) + "..." : "null") + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
