package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.UserPostRequest;
import com.slc.mentoring.dto.response.UserGetResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/check")
    public ResponseEntity<Boolean> amIadmin(@SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        return ResponseEntity.ok(true);
    }

    @PostMapping("/admin/user")
    public ResponseEntity<UserPostResponse> signup(@RequestBody UserPostRequest userPostRequest) {
        UserPostResponse userPostResponse = userService.signup(userPostRequest);
        return ResponseEntity.ok(userPostResponse);
    }

    @GetMapping("/admin/user")
    public ResponseEntity<UserGetResponse> showUsers() {
        UserGetResponse userGetResponse = userService.showUsers();
        return ResponseEntity.ok(userGetResponse);
    }

    @DeleteMapping("/admin/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserPostResponse> login(@RequestBody UserPostRequest userPostRequest,
                                                  HttpServletRequest httpServletRequest) {
        UserPostResponse userPostResponse = userService.login(userPostRequest);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("LOGIN_USER", userPostResponse);
        return ResponseEntity.ok(userPostResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/user/batch")
    public ResponseEntity<Void> createUserByCSV(@RequestParam("file") MultipartFile file) {
        userService.createUserByCSV(file);
        return ResponseEntity.ok().build();
    }
}
