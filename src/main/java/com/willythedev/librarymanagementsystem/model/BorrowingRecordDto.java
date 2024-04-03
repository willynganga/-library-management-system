package com.willythedev.librarymanagementsystem.model;

/** DTO for {@link BorrowingRecord} */
public record BorrowingRecordDto(String id, BookDto book, PatronDto patron) {}
