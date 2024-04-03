package com.willythedev.librarymanagementsystem.util;

public class StringConstantsUtil {
  public static final String BOOK_FOUND = "Book Found";
  public static final String ADDED_BOOK_SUCCESSFULLY = "Added book successfully";
  public static final String BOOK_EXISTS_WITH_ISBN = "There is a book with the same ISBN";
  public static final String BOOK_SAVED_SUCCESSFULLY = "Book saved successfully";
  public static final String BOOK_DELETED_SUCCESSFULLY = "Book deleted successfully";
  public static final String LIST_OF_BOOKS = "List of books";
  public static final String CURRENT_PAGE = "currentPage";
  public static final String TOTAL_PAGES = "totalPages";
  public static final String BOOK_WITH_ID_NOT_FOUND = "Book with Id not found";
  public static final String PATRON_WITH_ID_NOT_FOUND = "Patron with Id not found";
  public static final String PATRON_UNIQUE_EMAIL_OR_PHONE_NUMBER =
      "Patron with email or phone number exists. Please provide a unique email or phone number.";
  public static final String PATRON_SAVED_SUCCESSFULLY = "Patron saved successfully";
  public static final String PATRON_UPDATED_SUCCESSFULLY = "Patron updated successfully";
  public static final String PATRON_DELETED_SUCCESSFULLY = "Patron deleted successfully";
  public static final String AUTHOR_VALIDATION_MSG =
      "Author name should only contain alphabet only with a minimum length of 3 and a maximum length of 50.";
  public static final String TITLE_VALIDATION_MESSAGE =
      "Title should only contain alphabet and numbers with a minimum length of 3 and a maximum length of 50.";
  public static final String ISBN_VALIDATION_MESSAGE =
      "ISBN should only contain alphabet, numbers and hyphens only with a minimum length of 3 and a maximum length of 50.";
  public static final String PATRON_NAME_VALIDATION_MSG =
      "Name should only contain alphabet only with a minimum length of 3 and a maximum length of 50.";
  public static final String PATRON_EMAIL_VALIDATION_MSG = "A valid email is required";
  public static final String PATRON_ADDRESS_VALIDATION_MSG =
      "Address should only contain alphabet, numbers, hyphens and full stops with a minimum length of 3 and a maximum length of 50.";
  public static final String PATRON_PHONE_NUMBER_VALIDATION_MSG =
      "Phone number should consist digits only";

  private StringConstantsUtil() throws IllegalAccessException {
    throw new IllegalAccessException("This is a util class");
  }
}
