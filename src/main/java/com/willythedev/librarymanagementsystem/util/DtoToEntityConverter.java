package com.willythedev.librarymanagementsystem.util;

import com.willythedev.librarymanagementsystem.model.Book;
import com.willythedev.librarymanagementsystem.model.Patron;
import com.willythedev.librarymanagementsystem.wrapper.CreateBookDto;
import com.willythedev.librarymanagementsystem.wrapper.CreatePatronDto;
import java.util.function.Function;

public class DtoToEntityConverter {
  private DtoToEntityConverter() throws IllegalAccessException {
    throw new IllegalAccessException("This is a utility class");
  }

  public static final Function<CreateBookDto, Book> convertCreateBookDtoToBook =
      bookDto ->
          Book.builder()
              .author(bookDto.author())
              .isbn(bookDto.isbn())
              .title(bookDto.title())
              .publicationYear(bookDto.publicationYear())
              .build();

  public static final Function<CreatePatronDto, Patron> convertCreatePatronDtoToPatron =
      patronDto ->
          Patron.builder()
              .address(patronDto.address())
              .name(patronDto.name())
              .email(patronDto.email())
              .phoneNumber(patronDto.phoneNumber())
              .build();
}
