package com.example.demo.service;

import com.example.demo.model.Stock;
import com.example.demo.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    private Path root = Paths.get("uploads");

    @Autowired
    StockRepository stockRepository;

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
    public String save(MultipartFile file) throws IOException {
        Path filePath = root.resolve(file.getOriginalFilename());
        try{
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                throw new RuntimeException("File Already exists");
            } else {
                Files.copy(file.getInputStream(), filePath);
                stockRepository.saveAll(readDataForDB(filePath.getFileName().toString()));
                return "File Uploaded";
            }
        } catch (Exception e) {
            FileSystemUtils.deleteRecursively(filePath);
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }

    @Override
    public List<Stock> readData(String searchValue){
        return stockRepository.findAllByStock(searchValue);
        /*String data = new String(readDataFile(searchFile), StandardCharsets.UTF_8);
        List<String> dataList = Arrays.asList(data.split("\\r\\n"));
        List<String[]> filteredList = dataList.stream()
                .map(s -> s.split(","))
                .filter(stock -> stock[1].equalsIgnoreCase(searchValue))
                .collect(Collectors.toList());
        return filteredList;*/
    }

    public List<Stock> readDataForDB(String searchFile){
        String data = new String(readDataFile(searchFile), StandardCharsets.UTF_8);
        List<String> dataList = Arrays.asList(data.split("\\r\\n"));
        return dataList.stream()
                .map(s -> s.split(","))
                .skip(1)
                .map(ss -> stockBuilder(ss))
                .collect(Collectors.toList());
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
    public void saveRecord(String record){

        stockRepository.save(stockBuilder(record.split(",")));
        /*try{
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
        }*/
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

    private Stock stockBuilder(String[] stockRow){
        return Stock.builder()
                .quarter(stockRow[0])
                .stock(stockRow[1])
                .date(new Date(stockRow[2]))
                .open(stockRow[3])
                .high(stockRow[4])
                .low(stockRow[5])
                .close(stockRow[6])
                .volume(Long.parseLong(stockRow[7]))
                .percentageChangePrice(Double.parseDouble(stockRow[8]))
                .percentChangeVolumeOverLastWeek(!StringUtils.isEmpty(stockRow[9]) ? Double.parseDouble(stockRow[9]):Double.parseDouble("0.00"))
                .previousWeeksVolume(!StringUtils.isEmpty(stockRow[10]) ? Double.parseDouble(stockRow[10]):Double.parseDouble("0.00"))
                .nextWeeksOpen(stockRow[11])
                .nextWeeksClose(stockRow[12])
                .percentChangeNextWeeksPrice(!StringUtils.isEmpty(stockRow[13]) ? Double.parseDouble(stockRow[13]):Double.parseDouble("0.00"))
                .daysToNextDividend(!StringUtils.isEmpty(stockRow[14]) ? Integer.parseInt(stockRow[14]):Integer.parseInt("0"))
                .percentReturnNextDividend(!StringUtils.isEmpty(stockRow[15]) ? Double.parseDouble(stockRow[15]):Double.parseDouble("0.00"))
                .build();
    }
}
