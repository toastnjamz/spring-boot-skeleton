package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;

    @InjectMocks
    UserService userService;

    private User testUser;

    @Before
    public void setup() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword1!");
        testUser.setFullname("Test User");
        testUser.setRole("USER");
    }

//    @Test
//    public void getUserFromAuth_userLoggedIn_userReturned() {
//        // arrange
//        UserDetails userDetailsMock = new UserDetails(testUser, "USER");
//
//        Authentication auth = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(((UserDetails)(auth.getPrincipal()))).thenReturn(userDetailsMock);
//
//        when(userRepositoryMock.findUserByUsername(testUser.getUsername())).thenReturn(testUser);
//
//        //act
//        User result = userService.getUserFromAuth(auth);
//
//        //assert
//        assertNotNull(result);
//    }

    @Test
    public void getUserFromAuth_userNotLoggedIn_nullReturned() {
        //arrange
        AnonymousAuthenticationToken authentication = mock(AnonymousAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        //act
        User result = userService.getUserFromAuth(authentication);

        //assert
        assertNull(result);
    }

    @Test
    public void findAll_userExists_usersReturned() {
        // arrange
        List<User> users = new ArrayList<>();
        users.add(testUser);

        when(userRepositoryMock.findAll()).thenReturn(users);

        // act
        List<User> listResult = userService.findAll();

        // assert
        assertTrue(listResult.size() > 0);
    }

    @Test
    public void findAll_userDoesNotExists_noUsersReturned() {
        // arrange

        // act
        List<User> listResult = userService.findAll();

        // assert
        assertTrue(listResult.size() == 0);
    }

    @Test
    public void findUserByUsername_userExists_userReturned() {
        // arrange
        when(userRepositoryMock.findUserByUsername("testUser")).thenReturn(testUser);

        // act
        User userResult = userService.findUserByUsername("testUser");

        // assert
        assertEquals(testUser.getId(), userResult.getId(), 0);
    }

    @Test
    public void findUserByUsername_userDoesNotExist_nullReturned() {
        // arrange

        // act
        User userResult = userService.findUserByUsername("fakeUser");

        // assert
        assertNull(userResult);
    }

    @Test
    public void createUser_validUser_userReturned() {
        // arrange
        User testUser2 = new User();
        testUser2.setId(2);
        testUser2.setUsername("testUser2");
        testUser2.setPassword("testPassword2!");
        testUser2.setFullname("Test User 2");
        testUser2.setRole("USER");

        when(userRepositoryMock.save(testUser2)).thenReturn(testUser2);

        // act
        User userResult = userService.createUser(testUser2);

        // assert
        verify(userRepositoryMock, times(1)).save(any(User.class));
        assertEquals(testUser2.getId(), userResult.getId(), 0);
    }

    @Test
    public void updateUser_validUser_userReturned() {
        // arrange
        testUser.setUsername("updatedUserName");

        when(userRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(testUser));

        // act
        userService.updateUser(testUser);

        // assert
        verify(userRepositoryMock, times(1)).save(any(User.class));
        assertEquals(testUser.getUsername(), "updatedUserName");
    }

    @Test
    public void deleteUser_validUser_userDeleted() {
        // arrange

        // act
        userService.deleteUser(1);

        // assert
        Optional<User> user = userRepositoryMock.findById(1);
        assertFalse(user.isPresent());
    }
}
