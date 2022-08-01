package com.example.demo.integTest;

import com.example.demo.DataProcessingApplication;
import com.example.demo.executor.CommandLineTaskExecutor;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = DataProcessingApplication.class, properties = {
        "command.line.runner.enabled=false",
        "application.runner.enabled=false" })
public class DataProcessingIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    FileUploadService fileUploadService;

    @Mock
    Path path;

    @Autowired
    private WebApplicationContext webApplicationContext;

    /*@Autowired
    private ApplicationContext context;*/

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    File file;

    @Before
    public void setUp() throws IOException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Path tempPath = Files.createDirectories(temporaryFolder.getRoot().toPath());


        fileUploadService.deleteAll();
        Mockito.when(path.resolve(Mockito.anyString())).thenReturn(tempPath.resolve("dow_jones_index.data"));

    }

    @Test
    public void readData_whenValidRequest_validResponse() throws Exception {

        writeToFile();
        this.mvc.perform(MockMvcRequestBuilders.get("/v0/files?searchValue=RAH").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    public void writeToFile() throws IOException {
        file = temporaryFolder.newFile("dow_jones_index.data");

        FileWriter fw1 = new FileWriter( file );
        BufferedWriter bw1 = new BufferedWriter( fw1 );
        bw1.write( "1,RAH,1/7/2011,$13.85,$14.69,$13.80,$14.25,1453438639,2.88809,,,$14.17,$15.25,7.62174,54,0.0701754");
        bw1.close();
    }
}
