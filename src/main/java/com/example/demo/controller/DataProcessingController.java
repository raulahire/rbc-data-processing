package com.example.demo.controller;

import com.example.demo.model.Stock;
import com.example.demo.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class DataProcessingController {

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping(value = "/v0/data/processing")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file){

        try{
            fileUploadService.save(file);
            return ResponseEntity.status(HttpStatus.OK).body("File Uploaded");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }

    @GetMapping(value = "/v0/record")
    public ResponseEntity<List<Stock>> getRecord(@RequestParam("searchValue") String searchValue){
        return ResponseEntity.status(HttpStatus.OK).body(fileUploadService.readData(searchValue));
    }

    @PostMapping(value = "/v0/data")
    public ResponseEntity<String> addRecord(@RequestBody String data){
        try{
            fileUploadService.saveRecord(data);
            return ResponseEntity.status(HttpStatus.OK).body("Record Added");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
