package com.willythedev.librarymanagementsystem.model;

import jakarta.persistence.Column;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_users")
public class SystemUser {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String password;

  @CreatedBy protected String createdBy;
  @CreatedDate protected LocalDateTime createdOn;
  @LastModifiedBy protected String lastModifiedBy;
  @LastModifiedDate protected LocalDateTime lastModifiedDate;
  private int trials;
  private String role;

  @Column(unique = true)
  private String email;

  private boolean locked;
}
