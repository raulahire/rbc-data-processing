package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    private Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for uploads!");
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public String save(MultipartFile file) {
        try{
            Path filePath = root.resolve(file.getOriginalFilename());
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                throw new RuntimeException("File Already exists");
            } else {
                Files.copy(file.getInputStream(), filePath);
                return "File Uploaded";
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }

    @Override
    public List<String[]> readData(String searchValue, String searchFile){
        String data = new String(readDataFile(searchFile), StandardCharsets.UTF_8);
        List<String> dataList = Arrays.asList(data.split("\\r\\n"));
        List<String[]> filteredList = dataList.stream()
                .map(s -> s.split(","))
                .filter(stock -> stock[1].equalsIgnoreCase(searchValue))
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void saveRecord(String record, String searchFile){
        try{
            List<Path> filePathList = getFilePathList();
            Path filePath = filePathList.stream().filter(file -> file.getFileName().toString().equals(searchFile))
                    .findFirst().get();
            Resource resource = load(filePath.getFileName().toString());
            try {
                StringBuilder data = new StringBuilder(new String(readDataFile(searchFile), StandardCharsets.UTF_8));
                data.append(record);
                Files.write(resource.getFile().toPath(), Collections.singleton(data.toString()), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("IOException", e);
            }
        } catch(IOException e){
            throw new RuntimeException("Could not load the files!");
        }
    }

    public byte[] readDataFile(String searchFile){
        try{

            List<Path> filePathList = getFilePathList();
            Path filePath = filePathList.stream().filter(file -> file.getFileName().toString().equals(searchFile))
                        .findFirst().get();
            Resource resource = load(filePath.getFileName().toString());
            InputStream inputStream = resource.getInputStream();
            try {
                return FileCopyUtils.copyToByteArray(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("IOException", e);
            }
        } catch(IOException e){
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Path getFilePath(String searchFile) throws IOException {
        return Files.walk(this.root, 1)
                .filter(path -> !path.equals(this.root)).findFirst().get();
    }

    public List<Path> getFilePathList() throws IOException {
        return Files.walk(this.root, 1)
                .filter(path -> !path.equals(this.root)).collect(Collectors.toList());
    }
}
