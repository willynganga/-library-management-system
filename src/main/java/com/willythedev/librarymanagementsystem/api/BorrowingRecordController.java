package com.willythedev.librarymanagementsystem.api;

import com.willythedev.librarymanagementsystem.service.BorrowingRecordService;
import com.willythedev.librarymanagementsystem.util.ResponseEntityUtil;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class BorrowingRecordController {
  private final BorrowingRecordService borrowingRecordService;

  @PostMapping("/borrow/{bookId}/patron/{patronId}")
  @PreAuthorize("hasAnyAuthority('ROLE_PATRON')")
  public ResponseEntity<UniversalResponse> borrowBook(
      @PathVariable String bookId, @PathVariable String patronId) {
    return ResponseEntityUtil.getResponseEntity(
        borrowingRecordService.borrowBook(bookId, patronId));
  }

  @PutMapping("/return/{bookId}/patron/{patronId}")
  @PreAuthorize("hasAnyAuthority('ROLE_PATRON')")
  public ResponseEntity<UniversalResponse> returnBook(
      @PathVariable String bookId, @PathVariable String patronId) {
    return ResponseEntityUtil.getResponseEntity(
        borrowingRecordService.returnBook(bookId, patronId));
  }
}
