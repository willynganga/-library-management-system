package com.willythedev.librarymanagementsystem.util;

import com.willythedev.librarymanagementsystem.model.Book;
import com.willythedev.librarymanagementsystem.model.BookDto;
import com.willythedev.librarymanagementsystem.model.BorrowingRecord;
import com.willythedev.librarymanagementsystem.model.BorrowingRecordDto;
import com.willythedev.librarymanagementsystem.model.Patron;
import com.willythedev.librarymanagementsystem.model.PatronDto;
import java.util.function.Function;

public class EntityToDtoConverter {
  private EntityToDtoConverter() throws IllegalAccessException {
    throw new IllegalAccessException("This is a utility class");
  }

  public static Function<Book, BookDto> convertBookToBookDto =
      book ->
          new BookDto(
              book.getId(),
              book.getIsbn(),
              book.getTitle(),
              book.getAuthor(),
              book.getPublicationYear());

  public static Function<Patron, PatronDto> convertPatronToPatronDto =
      patron -> new PatronDto(patron.getId(), patron.getName());

  public static Function<BorrowingRecord, BorrowingRecordDto> convertBorrowingRecordToDto =
      borrowingRecord ->
          new BorrowingRecordDto(
              borrowingRecord.getId(),
              convertBookToBookDto.apply(borrowingRecord.getBook()),
              convertPatronToPatronDto.apply(borrowingRecord.getPatron()));
}
