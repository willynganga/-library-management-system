package com.willythedev.librarymanagementsystem.repository;

import com.willythedev.librarymanagementsystem.model.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface BookRepository extends JpaRepository<Book, String> {
  Optional<Book> findByIsbn(@NonNull String isbn);
}
