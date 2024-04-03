package com.willythedev.librarymanagementsystem.repository;

import com.willythedev.librarymanagementsystem.model.Patron;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, String> {
  Optional<Patron> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
