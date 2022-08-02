package com.example.demo.service;

import com.example.demo.model.Stock;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileUploadService {
    public void init();
    public void deleteAll();
    public String save(MultipartFile file) throws IOException;
    public List<Stock> readData(String searchValue);
    public Resource load(String fileName);
    public void saveRecord(String record);
}
