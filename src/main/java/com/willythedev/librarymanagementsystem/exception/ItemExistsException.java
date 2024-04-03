package com.willythedev.librarymanagementsystem.exception;

public class ItemExistsException extends RuntimeException {
  public ItemExistsException(String message) {
    super(message);
  }
}
