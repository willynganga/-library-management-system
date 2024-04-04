package com.willythedev.librarymanagementsystem.api;

import com.willythedev.librarymanagementsystem.service.PatronService;
import com.willythedev.librarymanagementsystem.util.ResponseEntityUtil;
import com.willythedev.librarymanagementsystem.wrapper.CreatePatronDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdatePatronDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patrons")
public class PatronController {
  private final PatronService patronService;

  @PostMapping
  @CacheEvict("patrons")
  public ResponseEntity<UniversalResponse> addPatron(
      @Valid @RequestBody CreatePatronDto createPatronDto) {
    return ResponseEntityUtil.getResponseEntity(patronService.addPatron(createPatronDto));
  }

  @CacheEvict("patrons")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<UniversalResponse> deletePatron(
      @PathVariable("id") @NotBlank @NotEmpty String patronId) {
    return ResponseEntityUtil.getResponseEntity(patronService.deletePatron(patronId));
  }

  @GetMapping
  @Cacheable("patrons")
  public ResponseEntity<UniversalResponse> findAllPatrons(
      @RequestParam int page, @RequestParam int size) {
    return ResponseEntityUtil.getResponseEntity(patronService.findAllPatrons(page, size));
  }

  @Cacheable("patrons")
  @GetMapping("/{id}")
  public ResponseEntity<UniversalResponse> findPatron(
      @PathVariable("id") @NotBlank @NotEmpty String patronId) {
    return ResponseEntityUtil.getResponseEntity(patronService.findPatron(patronId));
  }

  @CacheEvict("patrons")
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<UniversalResponse> updatePatron(
      @PathVariable("id") @NotBlank @NotEmpty String patronId,
      @Valid @RequestBody UpdatePatronDto updatePatronDto) {
    return ResponseEntityUtil.getResponseEntity(
        patronService.updatePatron(patronId, updatePatronDto));
  }
}
