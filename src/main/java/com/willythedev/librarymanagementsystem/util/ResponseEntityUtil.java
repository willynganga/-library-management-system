package com.willythedev.librarymanagementsystem.util;

import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {
  private ResponseEntityUtil() throws IllegalAccessException {
    throw new IllegalAccessException("Cannot instantiate a util class");
  }

  public static ResponseEntity<UniversalResponse> getResponseEntity(
      UniversalResponse universalResponse) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(universalResponse);
  }
}
