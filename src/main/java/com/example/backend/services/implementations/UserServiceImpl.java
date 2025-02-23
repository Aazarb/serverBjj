package com.example.backend.services.implementations;

import com.example.backend.dto.LoggedStatusDto;
import com.example.backend.dto.LoginResponseDto;
import com.example.backend.dto.UserDto;
import com.example.backend.enums.RoleEnum;
import com.example.backend.exceptions.*;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.CustomUserDetails;
import com.example.backend.services.EmailService;
import com.example.backend.services.UserService;
import com.example.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.example.backend.utils.validators.UserDtoValidator.validateForRegistration;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;

    }

    @Override
    @Transactional
    public UserDto register(UserDto userToRegister) throws DuplicateUserException {
        validateForRegistration(userToRegister);
        Optional<User> existingUserByEmail = this.userRepository.findByEmail(userToRegister.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new DuplicateUserException("User with email " + userToRegister.getEmail() + " already exists");
        }
        Optional<User> existingUserByUsername = this.userRepository.findByUsername(userToRegister.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new DuplicateUserException("User with username " + userToRegister.getUsername() + " already exists");
        }
        User newUser = new User(userToRegister.getUsername(), userToRegister.getEmail(), this.bCryptPasswordEncoder.encode(userToRegister.getPassword()), userToRegister.getRole());
        User newUserSaved = userRepository.save(newUser);

        String confirmationToken = this.jwtUtil.generateConfirmationToken(new CustomUserDetails(newUserSaved));

        String confirmationLink = appBaseUrl + "/auth/confirm?token=" + confirmationToken;

        this.emailService.sendVerificationEmail(newUser.getEmail(), confirmationLink, newUser.getUsername());

        return new UserDto(newUserSaved.getId(), newUserSaved.getUsername(), newUserSaved.getEmail(), newUserSaved.getRole());
    }

    @Override
    @Transactional
    public void confirmRegistration(String token) throws ConfirmationTokenNotFoundException {
        if (token == null || token.isEmpty()) {
            throw new ConfirmationTokenNotFoundException("No token found");
        }
        if (!jwtUtil.isTokenValid(token)) {
            throw new ConfirmationTokenNotFoundException("Token is not valid");
        }

        Map<String, Object> claims = jwtUtil.extractClaims(token);


        String username = (String) claims.get("sub");
        if (username == null || username.isEmpty()) {
            throw new ConfirmationTokenNotFoundException("Token does not contain a subject");
        }

        Optional<User> optionnalUserToConfirm = this.userRepository.findByUsername(claims.get("sub").toString());
        if (optionnalUserToConfirm.isEmpty()) {
            throw new ConfirmationTokenExpiredException("User " + claims.get("sub").toString() + " is not found");
        }
        User userToConfirm = optionnalUserToConfirm.get();

        userToConfirm.setEnabled(true);
        userRepository.save(userToConfirm);
        this.emailService.sendConfirmationEmail(userToConfirm.getEmail(), userToConfirm.getUsername());
    }

    @Override
    public LoginResponseDto login(UserDto userDto) throws UserNotFoundException {
        Optional<User> optionnalUserToLogin = this.userRepository.findByEmail(userDto.getEmail());
        if(optionnalUserToLogin.isEmpty()) {
            throw new UserNotFoundException("User " + userDto.getEmail() + " not found");
        }

        User userToLogin = optionnalUserToLogin.get();

        if(!this.bCryptPasswordEncoder.matches(userDto.getPassword(),userToLogin.getPassword())){
            throw new IncorrectPasswordException("Password is incorrect");
        }else{
            String token = this.jwtUtil.generateToken(new CustomUserDetails(userToLogin));
            return new  LoginResponseDto(token,new UserDto(userToLogin.getId(), userToLogin.getUsername(), userToLogin.getEmail(), userToLogin.getRole()));
        }
    }

    @Override
    public LoggedStatusDto checkStatusAndRole(String token) throws UserNotFoundException {
        boolean isTokenValid = jwtUtil.isTokenValid(token);
        RoleEnum role;
        Optional<User> optionnalUserToCheck = this.userRepository.findByUsername(this.jwtUtil.extractUsername(token));
        if(optionnalUserToCheck.isPresent()){
            role = optionnalUserToCheck.get().getRole();
        }else{
            throw new UserNotFoundException("User with username: " + this.jwtUtil.extractUsername(token) + "  not found");
        }
        return new LoggedStatusDto(isTokenValid,role);
    }
}
