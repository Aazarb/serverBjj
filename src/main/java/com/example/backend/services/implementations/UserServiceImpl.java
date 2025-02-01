package com.example.backend.services.implementations;

import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.DuplicateUserException;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.backend.utils.validators.UserDtoValidator.validateForRegistration;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto register(UserDto userToRegister) {
        validateForRegistration(userToRegister);
        Optional<User> existingUserByEmail = this.userRepository.findByEmail(userToRegister.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new DuplicateUserException("User with email " + userToRegister.getEmail() + " already exists");
        }
        Optional<User> existingUserByUsername = this.userRepository.findByUsername(userToRegister.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new DuplicateUserException("User with username " + userToRegister.getUsername() + " already exists");
        }
        User newUser = new User(userToRegister.getUsername(),userToRegister.getEmail(), this.bCryptPasswordEncoder.encode(userToRegister.getPassword()),userToRegister.getRole());
        User newUserSaved = userRepository.save(newUser);

        return new UserDto(newUserSaved.getId(),newUserSaved.getUsername(),newUserSaved.getEmail(),newUserSaved.getRole());
    }

    @Override
    public Optional<User> loadUser(String username) {
        return this.userRepository.findByUsername(username);
    }
}
