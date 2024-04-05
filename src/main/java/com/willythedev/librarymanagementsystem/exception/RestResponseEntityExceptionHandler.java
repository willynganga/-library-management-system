package com.willythedev.librarymanagementsystem.exception;

import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
  @ExceptionHandler(value = CustomAuthenticationException.class)
  public ResponseEntity<UniversalResponse> handleCustomAuthenticationException(
      CustomAuthenticationException customAuthenticationException) {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new UniversalResponse(401, customAuthenticationException.getMessage(), Map.of()));
  }

  @ExceptionHandler(value = ItemExistsException.class)
  public ResponseEntity<UniversalResponse> handleItemExistsException(
      ItemExistsException itemExistsException) {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new UniversalResponse(400, itemExistsException.getMessage(), Map.of()));
  }

  @ExceptionHandler(value = ItemNotFoundException.class)
  public ResponseEntity<UniversalResponse> handleItemNotFoundException(
      ItemNotFoundException itemNotFoundException) {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new UniversalResponse(404, itemNotFoundException.getMessage(), Map.of()));
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<UniversalResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    Map<String, String> errors = new HashMap<>();
    methodArgumentNotValidException
        .getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new UniversalResponse(400, "Validation errors", Map.of("errors", errors)));
  }
}
