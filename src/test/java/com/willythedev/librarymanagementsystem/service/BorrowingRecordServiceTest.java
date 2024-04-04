package com.willythedev.librarymanagementsystem.service;

import com.willythedev.librarymanagementsystem.exception.ItemNotFoundException;
import com.willythedev.librarymanagementsystem.model.Book;
import com.willythedev.librarymanagementsystem.model.BorrowingRecord;
import com.willythedev.librarymanagementsystem.model.Patron;
import com.willythedev.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
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

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {
  private static Book getBook() {
    return Book.builder()
        .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
        .title("Sample Book")
        .author("Awesome Author")
        .isbn("foo")
        .publicationYear(2024)
        .build();
  }

  private static BorrowingRecord getBorrowingRecord() {
    return BorrowingRecord.builder()
        .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a87")
        .patron(getPatron())
        .book(getBook())
        .returned(false)
        .build();
  }

  private static Patron getPatron() {
    return Patron.builder()
        .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a86")
        .phoneNumber("254712345678")
        .address("Street 171")
        .email("test@test.com")
        .name("Jane Doe")
        .build();
  }

  @Mock BookService bookService;
  @Mock BorrowingRecordRepository borrowingRecordRepository;
  @Mock PatronService patronService;
  private BorrowingRecordService borrowingRecordService;

  @Test
  void canBorrowBookTest() {
    final String bookId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a85";
    final String patronId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a86";
    Book book = getBook();
    Patron patron = getPatron();
    BorrowingRecord borrowingRecord = getBorrowingRecord();

    Mockito.when(bookService.getBookById(bookId)).thenReturn(book);
    Mockito.when(patronService.getPatronById(patronId)).thenReturn(patron);
    Mockito.when(borrowingRecordRepository.save(ArgumentMatchers.any()))
        .thenReturn(borrowingRecord);

    UniversalResponse universalResponse = borrowingRecordService.borrowBook(bookId, patronId);

    ArgumentCaptor<String> bookIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> patronIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(bookService).getBookById(bookIdArgumentCaptor.capture());
    Mockito.verify(patronService).getPatronById(patronIdArgumentCaptor.capture());

    Assertions.assertEquals(bookId, bookIdArgumentCaptor.getValue());
    Assertions.assertEquals(patronId, patronIdArgumentCaptor.getValue());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Record saved successfully", universalResponse.message());
    verifyNoMoreInteractions();
  }

  @Test
  void canReturnBook() {
    final String bookId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a85";
    final String patronId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a86";
    BorrowingRecord borrowingRecord = getBorrowingRecord();

    Mockito.when(borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId))
        .thenReturn(Optional.of(borrowingRecord));
    Mockito.when(borrowingRecordRepository.save(ArgumentMatchers.any()))
        .thenReturn(borrowingRecord);

    UniversalResponse universalResponse = borrowingRecordService.returnBook(bookId, patronId);

    ArgumentCaptor<BorrowingRecord> borrowingRecordArgumentCaptor =
        ArgumentCaptor.forClass(BorrowingRecord.class);
    Mockito.verify(borrowingRecordRepository).save(borrowingRecordArgumentCaptor.capture());

    Assertions.assertTrue(borrowingRecordArgumentCaptor.getValue().isReturned());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Record updated successfully", universalResponse.message());
    verifyNoMoreInteractions();
  }

  @Test
  void cannotBorrowBookForPatronThatDoesNotExistTest() {
    final String bookId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a85";
    final String patronId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a86";
    Book book = getBook();

    Mockito.when(bookService.getBookById(bookId)).thenReturn(book);
    Mockito.when(patronService.getPatronById(patronId))
        .thenThrow(new ItemNotFoundException("Patron with Id not found"));

    Assertions.assertThrows(
        ItemNotFoundException.class, () -> borrowingRecordService.borrowBook(bookId, patronId));

    verifyNoMoreInteractions();
  }

  @Test
  void cannotBorrowBookThatDoesNotExistTest() {
    final String bookId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a85";
    final String patronId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a86";

    Mockito.when(bookService.getBookById(bookId))
        .thenThrow(new ItemNotFoundException("Patron with Id not found"));

    Assertions.assertThrows(
        ItemNotFoundException.class, () -> borrowingRecordService.borrowBook(bookId, patronId));

    verifyNoMoreInteractions();
  }

  @Test
  void cannotReturnBookIfRecordDoesNotExistByBookAndPatronIdTest() {
    final String bookId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a85";
    final String patronId = "f8f78e70-a8cf-4766-baf9-bfcc7d671a86";

    Mockito.when(borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId))
        .thenReturn(Optional.empty());

    ItemNotFoundException itemNotFoundException =
        Assertions.assertThrows(
            ItemNotFoundException.class, () -> borrowingRecordService.returnBook(bookId, patronId));
    Assertions.assertEquals("Borrowing record not found", itemNotFoundException.getMessage());
    verifyNoMoreInteractions();
  }

  @BeforeEach
  void setUp() {
    borrowingRecordService =
        new BorrowingRecordService(bookService, borrowingRecordRepository, patronService);
  }

  private void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(bookService);
    Mockito.verifyNoMoreInteractions(patronService);
    Mockito.verifyNoMoreInteractions(borrowingRecordRepository);
  }
}
