package com.willythedev.librarymanagementsystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_borrowing_record")
public class BorrowingRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @CreatedBy protected String createdBy;
  @CreatedDate protected LocalDateTime createdOn;
  @LastModifiedBy protected String lastModifiedBy;
  @LastModifiedDate protected LocalDateTime lastModifiedDate;
  private boolean returned;

  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private Book book;

  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private Patron patron;
}
