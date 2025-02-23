package com.example.backend.controllers;

import com.example.backend.dto.LoggedStatusDto;
import com.example.backend.dto.LoginResponseDto;
import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.CookieNotFoundException;
import com.example.backend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto userToRegisterDto = new UserDto(userDto.getUsername(),userDto.getEmail(),userDto.getPassword());
        return new ResponseEntity<>(userService.register(userToRegisterDto), HttpStatus.CREATED);
    }

    @GetMapping(value="/confirm",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmRegistration(@RequestParam String token) {
        userService.confirmRegistration(token);
        return  ResponseEntity.ok().build();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto userDto, HttpServletResponse response) {

        LoginResponseDto loginResponseDto = userService.login(userDto);

        Cookie cookie = new Cookie("authToken", loginResponseDto.getToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);  disabled for local dev
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        loginResponseDto.setToken(null);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoggedStatusDto> checkIfLoggedIn(HttpServletRequest request) throws CookieNotFoundException {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("authToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
            return new ResponseEntity<>(userService.checkStatusAndRole(token),HttpStatus.ACCEPTED);
        }else{
            throw  new CookieNotFoundException("Cookie not found");
        }
    }
}