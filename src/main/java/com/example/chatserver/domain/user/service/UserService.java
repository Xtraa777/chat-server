package com.example.chatserver.domain.user.service;

import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.UserDto.Response;
import com.example.chatserver.domain.user.entity.User;
import com.example.chatserver.domain.user.repository.UserRepository;
import com.example.chatserver.global.exception.BusinessException;
import com.example.chatserver.global.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto.Response createUser(UserDto.Request userReqDto) {

        if (userRepository.existsByUsername(userReqDto.getUsername())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
        if (userRepository.existsByEmail(userReqDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = userReqDto.toEntity();
        User savedUser = userRepository.save(user);

        log.info("사용자 생성 완료: id={}, username={}", savedUser.getId(), savedUser.getUsername());

        return UserDto.Response.from(savedUser);
    }

    public UserDto.Response getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserDto.Response.from(user);
    }

    public List<Response> getAllUsers() {
        return  userRepository.findAll().stream()
            .map(UserDto.Response::from)
            .collect(Collectors.toList());
    }
}
