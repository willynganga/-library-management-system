package com.willythedev.librarymanagementsystem.service;

import com.willythedev.librarymanagementsystem.exception.ItemExistsException;
import com.willythedev.librarymanagementsystem.exception.ItemNotFoundException;
import com.willythedev.librarymanagementsystem.model.Patron;
import com.willythedev.librarymanagementsystem.model.PatronDto;
import com.willythedev.librarymanagementsystem.repository.PatronRepository;
import com.willythedev.librarymanagementsystem.util.DtoToEntityConverter;
import com.willythedev.librarymanagementsystem.util.EntityToDtoConverter;
import com.willythedev.librarymanagementsystem.util.StringConstantsUtil;
import com.willythedev.librarymanagementsystem.wrapper.CreatePatronDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdatePatronDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatronService {
  private final PatronRepository patronRepository;

  @Transactional
  public UniversalResponse addPatron(CreatePatronDto createPatronDto) {
    Optional<Patron> patronOptional =
        patronRepository.findByEmailOrPhoneNumber(
            createPatronDto.email(), createPatronDto.phoneNumber());

    if (patronOptional.isPresent()) {
      throw new ItemExistsException(StringConstantsUtil.PATRON_UNIQUE_EMAIL_OR_PHONE_NUMBER);
    }

    Patron patron = DtoToEntityConverter.convertCreatePatronDtoToPatron.apply(createPatronDto);
    Patron savedPatron = patronRepository.save(patron);

    return new UniversalResponse(
        200,
        StringConstantsUtil.PATRON_SAVED_SUCCESSFULLY,
        EntityToDtoConverter.convertPatronToPatronDto.apply(savedPatron));
  }

  @Transactional
  public UniversalResponse deletePatron(String patronId) {
    if (!patronRepository.existsById(patronId)) {
      throw new ItemNotFoundException(StringConstantsUtil.PATRON_WITH_ID_NOT_FOUND);
    }

    patronRepository.deleteById(patronId);

    return new UniversalResponse(200, StringConstantsUtil.PATRON_DELETED_SUCCESSFULLY, Map.of());
  }

  public UniversalResponse findAllPatrons(int page, int size) {
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(50, size));
    Page<Patron> patronPage = patronRepository.findAll(pageable);
    List<PatronDto> patrons =
        patronPage.getContent().stream()
            .map(patron -> EntityToDtoConverter.convertPatronToPatronDto.apply(patron))
            .toList();

    return new UniversalResponse(
        200,
        "Patrons List",
        Map.of(
            StringConstantsUtil.CURRENT_PAGE,
            patronPage.getNumber(),
            StringConstantsUtil.TOTAL_PAGES,
            patronPage.getTotalPages(),
            "patrons",
            patrons));
  }

  public UniversalResponse findPatron(String patronId) {
    Patron patron = getPatronById(patronId);

    return new UniversalResponse(
        200, "Patron Found", EntityToDtoConverter.convertPatronToPatronDto.apply(patron));
  }

  public Patron getPatronById(String patronId) {
    Optional<Patron> patronOptional = patronRepository.findById(patronId);

    if (patronOptional.isEmpty()) {
      throw new ItemNotFoundException(StringConstantsUtil.PATRON_WITH_ID_NOT_FOUND);
    }

    return patronOptional.get();
  }

  @Transactional
  public UniversalResponse updatePatron(String patronId, UpdatePatronDto updatePatronDto) {
    Patron patron = getPatronById(patronId);

    patron.setAddress(
        updatePatronDto.address() != null ? updatePatronDto.address() : patron.getAddress());
    patron.setName(updatePatronDto.name() != null ? updatePatronDto.address() : patron.getName());
    patron.setEmail(updatePatronDto.email() != null ? updatePatronDto.email() : patron.getEmail());
    patron.setName(
        updatePatronDto.phoneNumber() != null
            ? updatePatronDto.phoneNumber()
            : patron.getPhoneNumber());
    Patron savedPatron = patronRepository.save(patron);

    return new UniversalResponse(
        200,
        StringConstantsUtil.PATRON_UPDATED_SUCCESSFULLY,
        EntityToDtoConverter.convertPatronToPatronDto.apply(savedPatron));
  }
}
