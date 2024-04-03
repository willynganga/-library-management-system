package com.willythedev.librarymanagementsystem.wrapper;

import com.willythedev.librarymanagementsystem.model.SystemUser;
import java.io.Serializable;

/** DTO for {@link SystemUser} */
public record CreateSystemUserDto(String password, String email) implements Serializable {}
