package com.willythedev.librarymanagementsystem.service;

import com.willythedev.librarymanagementsystem.exception.ItemExistsException;
import com.willythedev.librarymanagementsystem.exception.ItemNotFoundException;
import com.willythedev.librarymanagementsystem.model.Book;
import com.willythedev.librarymanagementsystem.model.BookDto;
import com.willythedev.librarymanagementsystem.repository.BookRepository;
import com.willythedev.librarymanagementsystem.util.DtoToEntityConverter;
import com.willythedev.librarymanagementsystem.util.EntityToDtoConverter;
import com.willythedev.librarymanagementsystem.util.StringConstantsUtil;
import com.willythedev.librarymanagementsystem.wrapper.CreateBookDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdateBookDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;

  @Transactional
  public UniversalResponse addBook(CreateBookDto createBookDto) {
    Optional<Book> bookWithIsbn = bookRepository.findByIsbn(createBookDto.isbn());

    if (bookWithIsbn.isPresent()) {
      throw new ItemExistsException(StringConstantsUtil.BOOK_EXISTS_WITH_ISBN);
    }

    Book book = DtoToEntityConverter.convertCreateBookDtoToBook.apply(createBookDto);
    Book savedBook = bookRepository.save(book);

    return new UniversalResponse(
        200,
        StringConstantsUtil.ADDED_BOOK_SUCCESSFULLY,
        EntityToDtoConverter.convertBookToBookDto.apply(savedBook));
  }

  @Transactional
  public UniversalResponse deleteBook(String bookId) {
    if (!bookRepository.existsById(bookId)) {
      throw new ItemNotFoundException(StringConstantsUtil.BOOK_WITH_ID_NOT_FOUND);
    }

    bookRepository.deleteById(bookId);

    return new UniversalResponse(200, StringConstantsUtil.BOOK_DELETED_SUCCESSFULLY, Map.of());
  }

  public UniversalResponse findAllBooks(int page, int size) {
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(50, size));
    Page<Book> booksPage = bookRepository.findAll(pageable);
    List<BookDto> books =
        booksPage.getContent().stream()
            .map(book -> EntityToDtoConverter.convertBookToBookDto.apply(book))
            .toList();

    return new UniversalResponse(
        200,
        StringConstantsUtil.LIST_OF_BOOKS,
        Map.of(
            StringConstantsUtil.CURRENT_PAGE,
            booksPage.getNumber(),
            StringConstantsUtil.TOTAL_PAGES,
            booksPage.getTotalPages(),
            "books",
            books));
  }

  public UniversalResponse findBook(String bookId) {
    Book book = getBookById(bookId);

    return new UniversalResponse(
        200, StringConstantsUtil.BOOK_FOUND, EntityToDtoConverter.convertBookToBookDto.apply(book));
  }

  public Book getBookById(String bookId) {
    Optional<Book> bookOptional = bookRepository.findById(bookId);

    if (bookOptional.isEmpty()) {
      throw new ItemNotFoundException(StringConstantsUtil.BOOK_WITH_ID_NOT_FOUND);
    }

    return bookOptional.get();
  }

  @Transactional
  public UniversalResponse updateBook(String bookId, UpdateBookDto updateBookDto) {
    Book book = getBookById(bookId);

    book.setAuthor(updateBookDto.author() != null ? updateBookDto.author() : book.getAuthor());
    book.setIsbn(updateBookDto.isbn() != null ? updateBookDto.isbn() : book.getIsbn());
    book.setTitle(updateBookDto.title() != null ? updateBookDto.title() : book.getTitle());
    book.setPublicationYear(
        updateBookDto.publicationYear() != 0
            ? updateBookDto.publicationYear()
            : book.getPublicationYear());

    Book savedBook = bookRepository.save(book);

    return new UniversalResponse(
        200,
        StringConstantsUtil.BOOK_SAVED_SUCCESSFULLY,
        EntityToDtoConverter.convertBookToBookDto.apply(savedBook));
  }
}
