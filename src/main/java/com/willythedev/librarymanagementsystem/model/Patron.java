package com.willythedev.librarymanagementsystem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
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
@Entity(name = "tb_patrons")
public class Patron {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @CreatedBy String createdBy;
  @CreatedDate LocalDateTime createdOn;
  @LastModifiedBy String lastModifiedBy;
  @LastModifiedDate LocalDateTime lastModifiedDate;
  private String name;

  @Column(unique = true)
  private String email;

  private String address;

  @Column(unique = true)
  private String phoneNumber;

  @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BorrowingRecord> borrowingRecord;
}
