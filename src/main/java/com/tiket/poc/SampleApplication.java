package com.tiket.poc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;

@SpringBootApplication
public class SampleApplication {
  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }

  /**
   * We have a folder with name sample in our bucket gs://spring-cloud-gcp-poc
   */
  private String folderPath = "gs://spring-cloud-gcp-poc/sample/";

  @Bean
  public ApplicationRunner writeAndReadFiles(ResourceLoader resourceLoader) {
    return args -> {
      String originalContent = UUID.randomUUID().toString();

      WritableResource writeResource = (WritableResource) resourceLoader.getResource(folderPath.concat("test.txt"));
      try(BufferedWriter contentWriter = new BufferedWriter(new OutputStreamWriter(writeResource.getOutputStream()))) {
        contentWriter.write(originalContent);
        contentWriter.flush();
      }

      Resource readResource = resourceLoader.getResource(folderPath.concat("test.txt"));
      try(BufferedReader contentReader = new BufferedReader(new InputStreamReader(readResource.getInputStream()))) {
        String contentLine = contentReader.readLine();
        if(originalContent.equals(contentLine)) {
          System.out.println("Equals");
        }
        else {
          System.out.println("Not equals");
        }
      }
    };
  }
}
