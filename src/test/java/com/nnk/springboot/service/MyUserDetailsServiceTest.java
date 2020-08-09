package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.MyUserDetailsService;
import com.nnk.springboot.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Test
    public void loadUserByUsername_userExists_myUserDetailsReturned() {
        // arrange
        User testUser = new User();
        testUser.setId(100);
        testUser.setUsername("testUserName");
        testUser.setPassword("testPassword1!");
        testUser.setFullname("testFullName");
        testUser.setRole("USER");

        when(userServiceMock.findUserByUsername("testUserName")).thenReturn(testUser);

        // act
        UserDetails result = myUserDetailsService.loadUserByUsername("testUserName");

        // assert
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_userDoesNotExist_throwsUsernameNotFoundException() {
        // arrange
        String username = "test@test.com";

        when(userServiceMock.findUserByUsername(username)).thenReturn(null);

        // act
        UserDetails result = myUserDetailsService.loadUserByUsername(username);

        // assert
        assertNull(result);
    }
}
