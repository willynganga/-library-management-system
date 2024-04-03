package com.willythedev.librarymanagementsystem.repository;

import com.willythedev.librarymanagementsystem.model.SystemUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemUserRepository extends JpaRepository<SystemUser, String> {
  Optional<SystemUser> findByEmail(String email);
}
