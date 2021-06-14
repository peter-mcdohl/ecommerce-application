package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() {
        when(encoder.encode("pass123")).thenReturn("hashedPass");

        CreateUserRequest requestData = new CreateUserRequest();
        requestData.setUsername("test");
        requestData.setPassword("pass123");
        requestData.setConfirmPassword("pass123");

        ResponseEntity<User> responseEntity = userController.createUser(requestData);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedPass", user.getPassword());
    }

    @Test
    public void createUserInvalidPassword() {
        CreateUserRequest requestData = new CreateUserRequest();
        requestData.setUsername("test");
        requestData.setPassword("pass");
        requestData.setConfirmPassword("pass");

        ResponseEntity<User> responseEntity = userController.createUser(requestData);
        assertEquals(400, responseEntity.getStatusCodeValue());

        requestData.setPassword("pass123");
        requestData.setConfirmPassword("pass234");

        responseEntity = userController.createUser(requestData);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        return user;
    }

    @Test
    public void findById() {
        ResponseEntity<User> responseEntity = userController.findById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(getUser()));
        responseEntity = userController.findById(1L);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void findByUsername() {
        ResponseEntity<User> responseEntity = userController.findByUserName("test");
        assertEquals(404, responseEntity.getStatusCodeValue());

        when(userRepository.findByUsername("test")).thenReturn(getUser());
        responseEntity = userController.findByUserName("test");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
    }
}
