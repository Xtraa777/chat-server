package com.example.chatserver.domain.user.controller;

import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.UserDto.Response;
import com.example.chatserver.domain.user.repository.UserRepository;
import com.example.chatserver.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto.Response> createUser(
        @Valid @RequestBody UserDto.Request userReqDto) {
        UserDto.Response userResDto = userService.createUser(userReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getUser(@PathVariable Long id) {
        UserDto.Response response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Response>> getAllUsers() {
        List<UserDto.Response> responses = userService.getAllUsers();

        return ResponseEntity.ok(responses);
    }

}
