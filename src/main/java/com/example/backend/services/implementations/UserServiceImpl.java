package com.example.backend.services.implementations;

import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.ConfirmationTokenExpiredException;
import com.example.backend.exceptions.ConfirmationTokenNotFoundException;
import com.example.backend.exceptions.DuplicateUserException;
import com.example.backend.models.ConfirmationToken;
import com.example.backend.models.User;
import com.example.backend.repositories.ConfirmationTokenRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.CustomUserDetails;
import com.example.backend.services.UserService;
import com.example.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.example.backend.utils.validators.UserDtoValidator.validateForRegistration;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailServiceImpl emailServiceImpl;
    private final JwtUtil jwtUtil;
    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailServiceImpl emailServiceImpl, JwtUtil jwtUtil, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailServiceImpl = emailServiceImpl;
        this.jwtUtil = jwtUtil;
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

        String confirmationToken = this.jwtUtil.generateConfirmationToken(new CustomUserDetails(newUserSaved));

        String confirmationLink = appBaseUrl + "/confirm?token=" + confirmationToken;

        this.emailServiceImpl.sendVerificationEmail(newUser.getEmail(),confirmationLink,newUser.getUsername());

        return new UserDto(newUserSaved.getId(),newUserSaved.getUsername(),newUserSaved.getEmail(),newUserSaved.getRole());
    }

    @Override
    public Optional<User> loadUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void confirmRegistration(String token) {
        Optional<ConfirmationToken> tokenBeforeConfirmation = this.confirmationTokenRepository.findByToken(token);

        if(tokenBeforeConfirmation.isEmpty()) {
            throw new ConfirmationTokenNotFoundException("Confirmation token not found in database");
        }

        if(tokenBeforeConfirmation.get().getConfirmationDate()!= null){
            throw new ConfirmationTokenNotFoundException("Token already confirmed");
        }
        if(!jwtUtil.isConfirmationToken(token)) {
            throw new ConfirmationTokenNotFoundException("Token is not a confirmation token");
        }

        if(jwtUtil.isTokenValid(token)) {
            ConfirmationToken confirmationToken = tokenBeforeConfirmation.get();
            confirmationToken.setConfirmationDate(new Date());
            confirmationTokenRepository.save(confirmationToken);
            User userConfirmed = confirmationToken.getUser();
            userConfirmed.setEnabled(true);
            userRepository.save(userConfirmed);
        }
        else {
            throw new ConfirmationTokenExpiredException("Confirmation token expired");
        }
    }
}
