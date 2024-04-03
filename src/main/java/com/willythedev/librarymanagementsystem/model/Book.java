package com.willythedev.librarymanagementsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @CreatedBy protected String createdBy;
  @CreatedDate protected LocalDateTime createdOn;
  @LastModifiedBy protected String lastModifiedBy;
  @LastModifiedDate protected LocalDateTime lastModifiedDate;
  private String isbn;
  private String title;
  private String author;
  private int publicationYear;
}
