package com.example.demo.executor;

import com.example.demo.service.FileUploadService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CommandLineTaskExecutor implements CommandLineRunner {
    private FileUploadService fileUploadService;

    public CommandLineTaskExecutor(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Override
    public void run(String... args) throws Exception {
        fileUploadService.deleteAll();
        fileUploadService.init();
    }
}
