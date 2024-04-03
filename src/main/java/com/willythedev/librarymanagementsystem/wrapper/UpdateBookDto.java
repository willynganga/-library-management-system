package com.willythedev.librarymanagementsystem.wrapper;

import com.willythedev.librarymanagementsystem.util.StringConstantsUtil;
import jakarta.validation.constraints.Pattern;

public record UpdateBookDto(
    @Pattern(regexp = "[A-Za-z0-9-]{3,25}", message = StringConstantsUtil.ISBN_VALIDATION_MESSAGE)
        String isbn,
    @Pattern(
            regexp = "[A-Za-z0-9\\s]{3,50}",
            message = StringConstantsUtil.TITLE_VALIDATION_MESSAGE)
        String title,
    @Pattern(regexp = "[A-Za-z0-9\\s]{3,50}", message = StringConstantsUtil.AUTHOR_VALIDATION_MSG)
        String author,
    int publicationYear) {}
