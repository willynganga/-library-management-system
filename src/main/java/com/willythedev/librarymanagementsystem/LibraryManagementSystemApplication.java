package com.willythedev.librarymanagementsystem;

import com.willythedev.librarymanagementsystem.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = AppProperties.class)
public class LibraryManagementSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(LibraryManagementSystemApplication.class, args);
  }
}
