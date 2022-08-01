package com.example.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileUploadService {
    public void init();
    public void deleteAll();
    public String save(MultipartFile file);
    public List<String[]> readData(String searchValue, String searchFile);
    public Resource load(String fileName);
    public void saveRecord(String record, String searchFile);
}
