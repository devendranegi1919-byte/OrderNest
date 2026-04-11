package com.ordernest.service;

import com.ordernest.dto.AuthResponseDTO;
import com.ordernest.dto.LoginRequestDTO;
import com.ordernest.dto.RegisterRequestDTO;
import com.ordernest.entity.User;
import com.ordernest.exception.UserAlreadyPresentException;
import com.ordernest.exception.UserNotFoundException;
import com.ordernest.exception.WrongCredentialsException;
import com.ordernest.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtService  jwtService;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, JwtService jwtService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO registerUser (RegisterRequestDTO registerRequestDTO) {
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new UserAlreadyPresentException("User with same email is already present");
        }

        if(userRepository.existsByPhoneNumber(registerRequestDTO.getPhoneNumber())) {
            throw new UserAlreadyPresentException("User with same phone number is already present");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(registerRequestDTO.getPassword());
        User user  = new User();
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        user.setRole(registerRequestDTO.getRole());
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getUserId(), savedUser.getRole());

        return new AuthResponseDTO(token, savedUser.getName(), savedUser.getRole());
    }

    public AuthResponseDTO loginUser (LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new WrongCredentialsException("Wrong password");
        }
        String token = jwtService.generateToken(user.getUserId(), user.getRole());

        return new AuthResponseDTO(token, user.getName(), user.getRole());
    }
}
