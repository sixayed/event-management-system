package org.example.event.service;

import org.example.event.service.model.UserEntity;
import org.example.event.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private final String login = "login";
    private final String email = "email@email.com";
    private final String password = "password";

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
    }

    @Test
    void findByLogin_ExistingLogin_ReturnsUser() {
        // arrange
        userRepository.save(user);

        // act
        Optional<UserEntity> result = userRepository.findByLogin(login);

        // assert
        assertTrue(result.isPresent());
        assertEquals(user.getLogin(), result.get().getLogin());
    }

    @Test
    void findByLogin_NonExistingLogin_ReturnsEmpty() {
        // act
        Optional<UserEntity> result = userRepository.findByLogin("nonExistingUser");

        // assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        // arrange
        userRepository.save(user);

        // act
        Optional<UserEntity> result = userRepository.findByEmail(email);

        // assert
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsEmpty() {
        // act
        Optional<UserEntity> result = userRepository.findByEmail("nonexisting@email.com");

        // assert
        assertFalse(result.isPresent());
    }

    @Test
    void saveUser_DuplicateLogin_ThrowsException() {
        // arrange
        userRepository.save(user);
        String anotherEmail = "anotheremail@email.com";

        UserEntity duplicateUser = new UserEntity();
        duplicateUser.setLogin(login);
        duplicateUser.setEmail(anotherEmail);
        duplicateUser.setPassword(password);

        // act & assert
        assertThrows(Exception.class, () -> userRepository.save(duplicateUser));
    }

    @Test
    void saveUser_DuplicateEmail_ThrowsException() {
        // arrange
        userRepository.save(user);
        String anotherLogin = "anotherLogin";

        UserEntity duplicateUser = new UserEntity();
        duplicateUser.setLogin(anotherLogin);
        duplicateUser.setEmail(email);
        duplicateUser.setPassword(password);

        // act & assert
        assertThrows(Exception.class, () -> userRepository.save(duplicateUser));
    }
}