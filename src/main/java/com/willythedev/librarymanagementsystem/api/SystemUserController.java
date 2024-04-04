package com.willythedev.librarymanagementsystem.api;

import com.willythedev.librarymanagementsystem.service.UserService;
import com.willythedev.librarymanagementsystem.util.ResponseEntityUtil;
import com.willythedev.librarymanagementsystem.wrapper.CreateSystemUserDto;
import com.willythedev.librarymanagementsystem.wrapper.LoginRequest;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SystemUserController {
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UniversalResponse> loginUser(@RequestBody LoginRequest loginRequest) {
    return ResponseEntityUtil.getResponseEntity(userService.loginUser(loginRequest));
  }

  @PostMapping("/admin")
  public ResponseEntity<UniversalResponse> registerAdmin(
      @RequestBody CreateSystemUserDto createSystemUserDto) {
    return ResponseEntityUtil.getResponseEntity(userService.registerAdmin(createSystemUserDto));
  }
}
