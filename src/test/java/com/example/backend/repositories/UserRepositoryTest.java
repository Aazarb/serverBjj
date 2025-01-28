package com.example.backend.repositories;

import com.example.backend.enums.RoleEnum;
import com.example.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        User user = new User(null, "JohnDoe", "john@example.com", "hashedPassword", RoleEnum.MEMBER);
        userRepository.save(user);
    }

    @Test
    void findByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        assertTrue(foundUser.isPresent());

    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        Optional<User> foundUser = userRepository.findByEmail("john@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailNotExists() {
        Optional<User> foundUser = userRepository.findByEmail("unknown@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("JohnDoe");
        assertTrue(foundUser.isPresent());
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUsernameExists() {
        Optional<User> foundUser = userRepository.findByUsername("JohnDoe");

        assertTrue(foundUser.isPresent());
        assertEquals("JohnDoe", foundUser.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUsernameNotExists() {
        Optional<User> foundUser = userRepository.findByUsername("Mike");
        assertFalse(foundUser.isPresent());
    }
}