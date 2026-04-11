package com.ordernest.service;

import com.ordernest.dto.AuthResponseDTO;
import com.ordernest.dto.RegisterRequestDTO;
import com.ordernest.entity.User;
import com.ordernest.exception.UserAlreadyPresentException;
import com.ordernest.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
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
        userRepository.save(user);

        //String token =

        return null;
    }
}
