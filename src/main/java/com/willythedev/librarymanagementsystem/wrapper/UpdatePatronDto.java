package com.willythedev.librarymanagementsystem.wrapper;

import com.willythedev.librarymanagementsystem.util.StringConstantsUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdatePatronDto(
    @Pattern(
            regexp = "[A-Za-z0-9\\s]{3,50}",
            message = StringConstantsUtil.PATRON_NAME_VALIDATION_MSG)
        String name,
    @Pattern(
            regexp = "[A-Za-z0-9\\s-.]{3,50}",
            message = StringConstantsUtil.PATRON_ADDRESS_VALIDATION_MSG)
        String address,
    @Email(message = StringConstantsUtil.PATRON_EMAIL_VALIDATION_MSG) String email,
    @Pattern(
            regexp = "\\d{12,15}",
            message = StringConstantsUtil.PATRON_PHONE_NUMBER_VALIDATION_MSG)
        String phoneNumber) {}
