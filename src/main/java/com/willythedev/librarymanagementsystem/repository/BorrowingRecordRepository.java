package com.willythedev.librarymanagementsystem.repository;

import com.willythedev.librarymanagementsystem.model.BorrowingRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, String> {
  Optional<BorrowingRecord> findByBookIdAndPatronId(String bookId, String patronId);
}
