package com.willythedev.librarymanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;

import com.willythedev.librarymanagementsystem.exception.ItemExistsException;
import com.willythedev.librarymanagementsystem.exception.ItemNotFoundException;
import com.willythedev.librarymanagementsystem.model.Book;
import com.willythedev.librarymanagementsystem.model.BookDto;
import com.willythedev.librarymanagementsystem.repository.BookRepository;
import com.willythedev.librarymanagementsystem.util.DtoToEntityConverter;
import com.willythedev.librarymanagementsystem.wrapper.CreateBookDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdateBookDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
  @Mock BookRepository bookRepository;

  private BookService bookService;

  @Test
  void canAddBookTest() {
    CreateBookDto createBookDto =
        new CreateBookDto("1-4028-9462-7", "Sample Book", "Awesome Author", 2024);
    Book book = DtoToEntityConverter.convertCreateBookDtoToBook.apply(createBookDto);

    Mockito.when(bookRepository.findByIsbn(createBookDto.isbn())).thenReturn(Optional.empty());
    Mockito.when(bookRepository.save(ArgumentMatchers.any())).thenReturn(book);

    UniversalResponse universalResponse = bookService.addBook(createBookDto);

    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
    Assertions.assertEquals(createBookDto.isbn(), bookArgumentCaptor.getValue().getIsbn());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Added book successfully", universalResponse.message());
    Assertions.assertNotNull(universalResponse.data());
  }

  @Test
  void canDeleteBookTest() {
    final String bookId = "1-4028-9462-7";

    Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

    UniversalResponse universalResponse = bookService.deleteBook(bookId);

    ArgumentCaptor<String> bookIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(bookRepository).deleteById(bookIdArgumentCaptor.capture());
    Assertions.assertEquals(bookId, bookIdArgumentCaptor.getValue());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Book deleted successfully", universalResponse.message());
  }

  @Test
  void canFindAllBooksTest() {
    final int page = 0;
    final int size = 10;
    List<Book> bookList =
        List.of(
            Book.builder()
                .title("Sample Book")
                .author("Awesome Author")
                .isbn("foo")
                .publicationYear(2024)
                .build());
    List<BookDto> expectedBooks =
        List.of(new BookDto(null, "foo", "Sample Book", "Awesome Author", 2024));

    Mockito.when(bookRepository.findAll(PageRequest.of(page, size)))
        .thenReturn(new PageImpl<>(bookList));

    UniversalResponse universalResponse = bookService.findAllBooks(page, size);

    ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
    Mockito.verify(bookRepository).findAll(pageableArgumentCaptor.capture());
    Assertions.assertEquals(0, pageableArgumentCaptor.getValue().getPageNumber());
    Assertions.assertEquals(10, pageableArgumentCaptor.getValue().getPageSize());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("List of books", universalResponse.message());
    Assertions.assertEquals(
        Map.of("currentPage", 0, "books", expectedBooks, "totalPages", 1),
        universalResponse.data());
  }

  @Test
  void canFindBookTest() {
    final String bookId = "foo";
    Book book =
        Book.builder()
            .title("Sample Book")
            .author("Awesome Author")
            .isbn("foo")
            .publicationYear(2024)
            .build();

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

    UniversalResponse universalResponse = bookService.findBook(bookId);

    ArgumentCaptor<String> bookIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(bookRepository).findById(bookIdArgumentCaptor.capture());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Book Found", universalResponse.message());
    Assertions.assertEquals(
        new BookDto(null, "foo", "Sample Book", "Awesome Author", 2024), universalResponse.data());
  }

  @Test
  void canUpdateBookTest() {
    final String bookId = "foo";
    UpdateBookDto updateBookDto = new UpdateBookDto("foo", "Awesome Book", "Awesome Author", 2024);
    Book book =
        Book.builder()
            .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
            .title("Sample Book")
            .author("Awesome Author")
            .isbn("foo")
            .publicationYear(2024)
            .build();

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
    Mockito.when(bookRepository.save(ArgumentMatchers.any())).thenReturn(book);

    UniversalResponse universalResponse = bookService.updateBook(bookId, updateBookDto);
    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
    ArgumentCaptor<String> bookIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(bookRepository).findById(bookIdArgumentCaptor.capture());

    Assertions.assertEquals(bookId, bookIdArgumentCaptor.getValue());
    Assertions.assertEquals(updateBookDto.isbn(), bookArgumentCaptor.getValue().getIsbn());
    Assertions.assertEquals(updateBookDto.author(), bookArgumentCaptor.getValue().getAuthor());
    Assertions.assertEquals(updateBookDto.title(), bookArgumentCaptor.getValue().getTitle());
    Assertions.assertEquals(
        updateBookDto.publicationYear(), bookArgumentCaptor.getValue().getPublicationYear());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Book saved successfully", universalResponse.message());
  }

  @Test
  void cannotAddBookWithNonUniqueIsbnTest() {
    CreateBookDto createBookDto =
        new CreateBookDto("1-4028-9462-7", "Sample Book", "Awesome Author", 2024);

    Mockito.when(bookRepository.findByIsbn(createBookDto.isbn()))
        .thenReturn(Optional.of(new Book()));

    Mockito.verify(bookRepository, Mockito.never()).save(ArgumentMatchers.any());
    ItemExistsException itemExistsException =
        assertThrows(
            ItemExistsException.class, () -> bookService.addBook(createBookDto), "Book exists");
    Assertions.assertEquals("There is a book with the same ISBN", itemExistsException.getMessage());
  }

  @Test
  void cannotDeleteBookThatDoesNotExist() {
    final String bookId = "foo";

    Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

    Mockito.verify(bookRepository, Mockito.never()).save(ArgumentMatchers.any());
    ItemNotFoundException itemNotFoundException =
        assertThrows(ItemNotFoundException.class, () -> bookService.deleteBook(bookId));
    Assertions.assertEquals("Book with Id not found", itemNotFoundException.getMessage());
  }

  @Test
  void cannotFindBookThatDoesNotExistTest() {
    final String bookId = "foo";

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

    ItemNotFoundException itemNotFoundException =
        assertThrows(ItemNotFoundException.class, () -> bookService.findBook(bookId));
    Assertions.assertEquals("Book with Id not found", itemNotFoundException.getMessage());
  }

  @Test
  void cannotUpdateBookThatDoesNotExistTest() {
    final String bookId = "foo";
    UpdateBookDto updateBookDto = new UpdateBookDto("foo", "Awesome Book", "Awesome Author", 2024);

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

    Mockito.verify(bookRepository, Mockito.never()).save(ArgumentMatchers.any());
    ItemNotFoundException itemNotFoundException =
        assertThrows(
            ItemNotFoundException.class, () -> bookService.updateBook(bookId, updateBookDto));
    Assertions.assertEquals("Book with Id not found", itemNotFoundException.getMessage());
    Mockito.verifyNoMoreInteractions(bookRepository);
  }

  @BeforeEach
  void setUp() {
    bookService = new BookService(bookRepository);
  }
}
