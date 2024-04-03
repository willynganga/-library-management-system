package com.willythedev.librarymanagementsystem.model;

/** DTO for {@link Book} */
public record BookDto(String id, String isbn, String title, String author, int publicationYear) {}
