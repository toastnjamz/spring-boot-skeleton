package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest(classes = {SpringSecurityTestConfig.class})
@RunWith(SpringRunner.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    private UserService userServiceMock;

    private User regularUser;
    private User adminUser;
    private Authentication auth;
    private SecurityContext securityContext;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();

        regularUser = new User();
        regularUser.setUsername("regularUser");
        regularUser.setPassword("testPassword1!");
        regularUser.setRole("USER");

        adminUser = new User();
        adminUser.setUsername("adminUser");
        adminUser.setPassword("testPassword1!");
        adminUser.setRole("ADMIN");

        auth = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void login_validURL_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("adminUser")
    public void getAllUserArticles_adminUser_viewIsUserList() throws Exception {
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(auth);
        when(userServiceMock.getUserFromAuth(auth)).thenReturn(adminUser);

        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(view().name("user/list"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void getAllUserArticles_regularUser_viewIs403() throws Exception {
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(auth);
        when(userServiceMock.getUserFromAuth(auth)).thenReturn(regularUser);

        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(view().name("403"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void error_validURL_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/app/error"))
                .andExpect(status().is2xxSuccessful());
    }
}
