package com.willythedev.librarymanagementsystem.api;

import com.willythedev.librarymanagementsystem.service.BookService;
import com.willythedev.librarymanagementsystem.util.ResponseEntityUtil;
import com.willythedev.librarymanagementsystem.wrapper.CreateBookDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdateBookDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
  private final BookService bookService;

  @PostMapping
  @CacheEvict("books")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<UniversalResponse> addBook(
      @Valid @RequestBody CreateBookDto createBookDto) {
    return ResponseEntityUtil.getResponseEntity(bookService.addBook(createBookDto));
  }

  @CacheEvict("books")
  @DeleteMapping("{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<UniversalResponse> deleteBook(@PathVariable("id") String bookId) {
    return ResponseEntityUtil.getResponseEntity(bookService.deleteBook(bookId));
  }

  @GetMapping
  @Cacheable("books")
  public ResponseEntity<UniversalResponse> findAllBooks(
      @RequestParam int page, @RequestParam int size) {
    return ResponseEntityUtil.getResponseEntity(bookService.findAllBooks(page, size));
  }

  @Cacheable("books")
  @GetMapping("/{id}")
  public ResponseEntity<UniversalResponse> findBook(
      @PathVariable("id") @NotBlank @NotEmpty String bookId) {
    return ResponseEntityUtil.getResponseEntity(bookService.findBook(bookId));
  }

  @CacheEvict("books")
  @PutMapping("{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<UniversalResponse> updateBook(
      @PathVariable("id") String bookId, @Valid @RequestBody UpdateBookDto bookDto) {
    return ResponseEntityUtil.getResponseEntity(bookService.updateBook(bookId, bookDto));
  }
}
