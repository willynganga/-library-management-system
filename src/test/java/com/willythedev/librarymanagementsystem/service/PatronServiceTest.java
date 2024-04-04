package com.willythedev.librarymanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;

import com.willythedev.librarymanagementsystem.exception.ItemExistsException;
import com.willythedev.librarymanagementsystem.exception.ItemNotFoundException;
import com.willythedev.librarymanagementsystem.model.Patron;
import com.willythedev.librarymanagementsystem.model.PatronDto;
import com.willythedev.librarymanagementsystem.repository.PatronRepository;
import com.willythedev.librarymanagementsystem.wrapper.CreatePatronDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdatePatronDto;
import java.util.List;
import java.util.Map;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {
  @Mock PatronRepository patronRepository;
  private PatronService patronService;

  @Test
  void canAddPatronTest() {
    CreatePatronDto createPatronDto =
        new CreatePatronDto("Jane Doe", "Street 171", "test@test.com", "254712345678");
    Patron patron =
        Patron.builder()
            .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
            .phoneNumber("254712345678")
            .address("Street 171")
            .email("test@test.com")
            .name("Jane Doe")
            .build();

    Mockito.when(
            patronRepository.findByEmailOrPhoneNumber(
                createPatronDto.email(), createPatronDto.phoneNumber()))
        .thenReturn(Optional.empty());
    Mockito.when(patronRepository.save(ArgumentMatchers.any())).thenReturn(patron);

    UniversalResponse universalResponse = patronService.addPatron(createPatronDto);

    ArgumentCaptor<Patron> patronArgumentCaptor = ArgumentCaptor.forClass(Patron.class);
    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> phoneNumberArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(patronRepository).save(patronArgumentCaptor.capture());
    Mockito.verify(patronRepository)
        .findByEmailOrPhoneNumber(
            emailArgumentCaptor.capture(), phoneNumberArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(patronRepository);

    Assertions.assertEquals(createPatronDto.email(), patronArgumentCaptor.getValue().getEmail());
    Assertions.assertEquals(
        createPatronDto.phoneNumber(), patronArgumentCaptor.getValue().getPhoneNumber());
    Assertions.assertEquals(createPatronDto.email(), emailArgumentCaptor.getValue());
    Assertions.assertEquals(createPatronDto.phoneNumber(), phoneNumberArgumentCaptor.getValue());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Patron saved successfully", universalResponse.message());
  }

  @Test
  void canDeletePatronTest() {
    final String patronId = "foo";

    Mockito.when(patronRepository.existsById(patronId)).thenReturn(true);

    UniversalResponse universalResponse = patronService.deletePatron(patronId);

    ArgumentCaptor<String> patronIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(patronRepository).deleteById(patronIdArgumentCaptor.capture());
    Assertions.assertEquals(patronId, patronIdArgumentCaptor.getValue());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Patron deleted successfully", universalResponse.message());
  }

  @Test
  void canFindPatronTest() {
    final String patronId = "foo";
    Patron patron =
        Patron.builder()
            .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
            .phoneNumber("254712345678")
            .address("Street 171")
            .email("test@test.com")
            .name("Jane Doe")
            .build();

    Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

    UniversalResponse universalResponse = patronService.findPatron(patronId);

    ArgumentCaptor<String> patronIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(patronRepository).findById(patronIdArgumentCaptor.capture());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Patron Found", universalResponse.message());
    Assertions.assertEquals(
        new PatronDto(
            "f8f78e70-a8cf-4766-baf9-bfcc7d671a85", "Jane Doe", "test@test.com", "254712345678"),
        universalResponse.data());
  }

  @Test
  void cannotAddPatronWithNonUniqueEmailOrPhoneNumberTest() {
    CreatePatronDto createPatronDto =
        new CreatePatronDto("Jane Doe", "Street 171", "test@test.com", "254712345678");
    Patron patron =
        Patron.builder()
            .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
            .phoneNumber("254712345678")
            .address("Street 171")
            .email("test@test.com")
            .name("Jane Doe")
            .build();

    Mockito.when(
            patronRepository.findByEmailOrPhoneNumber(
                createPatronDto.email(), createPatronDto.phoneNumber()))
        .thenReturn(Optional.of(patron));

    ItemExistsException itemExistsException =
        assertThrows(ItemExistsException.class, () -> patronService.addPatron(createPatronDto));
    Assertions.assertEquals(
        "Patron with email or phone number exists. Please provide a unique email or phone number.",
        itemExistsException.getMessage());
    Mockito.verifyNoMoreInteractions(patronRepository);
  }

  @Test
  void cannotDeletePatronThatDoesNotExist() {
    final String patronId = "foo";

    Mockito.when(patronRepository.existsById(patronId)).thenReturn(false);

    Mockito.verify(patronRepository, Mockito.never()).save(ArgumentMatchers.any());
    ItemNotFoundException itemNotFoundException =
        assertThrows(ItemNotFoundException.class, () -> patronService.deletePatron(patronId));
    Assertions.assertEquals("Patron with Id not found", itemNotFoundException.getMessage());
  }

  @Test
  void cannotFindPatronThatDoesNotExistTest() {
    final String patronId = "foo";

    Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

    ItemNotFoundException itemNotFoundException =
        assertThrows(ItemNotFoundException.class, () -> patronService.findPatron(patronId));
    Assertions.assertEquals("Patron with Id not found", itemNotFoundException.getMessage());
  }

  @Test
  void cannotUpdatePatronThatDoesNotExistTest() {
    final String patronId = "foo";
    UpdatePatronDto updatePatronDto =
        new UpdatePatronDto("Jane Doe", "Street 171", "test@test.com", "254712345678");

    Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

    Mockito.verify(patronRepository, Mockito.never()).save(ArgumentMatchers.any());
    ItemNotFoundException itemNotFoundException =
        assertThrows(
            ItemNotFoundException.class,
            () -> patronService.updatePatron(patronId, updatePatronDto));
    Assertions.assertEquals("Patron with Id not found", itemNotFoundException.getMessage());
    Mockito.verifyNoMoreInteractions(patronRepository);
  }

  @Test
  void findAllPatronsTest() {
    final int page = 0;
    final int size = 10;
    List<Patron> patronList =
        List.of(
            Patron.builder()
                .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
                .phoneNumber("254712345678")
                .address("Street 171")
                .email("test@test.com")
                .name("Jane Doe")
                .build());
    List<PatronDto> expectedPatrons =
        List.of(
            new PatronDto(
                "f8f78e70-a8cf-4766-baf9-bfcc7d671a85",
                "Jane Doe",
                "test@test.com",
                "254712345678"));

    Mockito.when(patronRepository.findAll(PageRequest.of(page, size)))
        .thenReturn(new PageImpl<>(patronList));

    UniversalResponse universalResponse = patronService.findAllPatrons(page, size);

    ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
    Mockito.verify(patronRepository).findAll(pageableArgumentCaptor.capture());
    Assertions.assertEquals(0, pageableArgumentCaptor.getValue().getPageNumber());
    Assertions.assertEquals(10, pageableArgumentCaptor.getValue().getPageSize());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Patrons List", universalResponse.message());
    Assertions.assertEquals(
        Map.of("currentPage", 0, "patrons", expectedPatrons, "totalPages", 1),
        universalResponse.data());
  }

  @BeforeEach
  void setUp() {
    patronService = new PatronService(patronRepository);
  }

  @Test
  void updatePatronTest() {
    final String patronId = "foo";
    UpdatePatronDto updatePatronDto =
        new UpdatePatronDto("Jane Doe", "Street 171", "test@test.com", "254712345678");
    Patron patron =
        Patron.builder()
            .id("f8f78e70-a8cf-4766-baf9-bfcc7d671a85")
            .phoneNumber("254712345678")
            .address("Street 171")
            .email("test@test.com")
            .name("Jane Doe")
            .build();

    Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
    Mockito.when(patronRepository.save(ArgumentMatchers.any())).thenReturn(patron);

    UniversalResponse universalResponse = patronService.updatePatron(patronId, updatePatronDto);
    ArgumentCaptor<Patron> patronArgumentCaptor = ArgumentCaptor.forClass(Patron.class);
    Mockito.verify(patronRepository).save(patronArgumentCaptor.capture());
    ArgumentCaptor<String> patronIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(patronRepository).findById(patronIdArgumentCaptor.capture());

    Assertions.assertEquals(patronId, patronIdArgumentCaptor.getValue());
    Assertions.assertEquals(updatePatronDto.name(), patronArgumentCaptor.getValue().getName());
    Assertions.assertEquals(updatePatronDto.email(), patronArgumentCaptor.getValue().getEmail());
    Assertions.assertEquals(
        updatePatronDto.phoneNumber(), patronArgumentCaptor.getValue().getPhoneNumber());
    Assertions.assertEquals(
        updatePatronDto.address(), patronArgumentCaptor.getValue().getAddress());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Patron updated successfully", universalResponse.message());
  }
}
