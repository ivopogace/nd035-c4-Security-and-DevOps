package com.example.demo.controllers;

import com.example.demo.SareetaApplication;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SareetaApplication.class)
public class UserControllerTest {

    private UserController userController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }


    @Test
    public void testCreateUserHappyPath(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest();

        ResponseEntity<?> response =  userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        User u = (User) response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void testFindUserById(){
        User r = newUser();
        when(userRepo.findById(r.getId())).thenReturn(Optional.of(r));
        ResponseEntity<User> response = userController.findById(r.getId());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(r.getId(), u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals(r.getPassword(), u.getPassword());
    }

    @Test
    public void testFindUserByUsername(){
        User r = newUser();
        when(userRepo.findByUsername(r.getUsername())).thenReturn(r);
        ResponseEntity<User> response = userController.findByUserName(r.getUsername());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals(r.getPassword(), u.getPassword());
    }

    private static CreateUserRequest createUserRequest() {
        CreateUserRequest r = new CreateUserRequest();
        Random random = new Random();
        String generatedString = random.ints(97, 122 + 1)
                .limit(7)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        r.setUsername(generatedString);
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        return r;
    }

    private static User newUser() {
        User r = new User();
        Random random = new Random();
        String generatedString = random.ints(97, 122 + 1)
                .limit(7)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        r.setId(1);
        r.setUsername(generatedString);
        r.setPassword("testPassword");
        return r;
    }

}
