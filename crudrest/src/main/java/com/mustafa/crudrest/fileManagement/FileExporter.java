package com.mustafa.crudrest.fileManagement;

import com.mustafa.crudrest.entity.Employee;

import java.io.IOException;
import java.util.List;

public interface FileExporter {
    byte[] export(List<Employee> employees) throws IOException;
    String getFileType();
    String getFileExtension();
}
