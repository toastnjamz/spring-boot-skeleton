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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {SpringSecurityTestConfig.class})
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    UserService userServiceMock;

    private User testUser;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        testUser = new User();
        testUser.setId(100);
        testUser.setUsername("testUserName");
        testUser.setPassword("testPassword1!");
        testUser.setFullname("testFullName");
        testUser.setRole("USER");
    }

    @Test
    @WithUserDetails("adminUser")
    public void home_validAdmin_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithUserDetails("adminUser")
    public void addUser_validAdmin_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("adminUser")
    public void validate_validUser_statusIsSuccessful() throws Exception {
        when(userServiceMock.createUser(testUser)).thenReturn(testUser);

        mockMvc.perform(post("/user/validate")
                .param("username", testUser.getUsername())
                .param("password", testUser.getPassword())
                .param("fullname", testUser.getFullname())
                .param("role", testUser.getRole()));

        mockMvc.perform(get("/user/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("adminUser")
    public void showUpdateForm_validUser_statusIsSuccessful() throws Exception {
        when(userServiceMock.findById(100)).thenReturn(testUser);

        mockMvc.perform(get("/user/update/" +100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("adminUser")
    public void updateUser_validUpdatedUser_statusIsRedirection() throws Exception {
        when(userServiceMock.findById(100)).thenReturn(testUser);

        mockMvc.perform(post("/user/update/" + 100)
                .param("username", "updatedUserName")
                .param("password", "updatedPassword1!")
                .param("fullname", "updatedFullName")
                .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/user/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("adminUser")
    public void deleteUser_validUser_statusIsRedirection() throws Exception {
        when(userServiceMock.findById(100)).thenReturn(testUser);

        mockMvc.perform(get("/user/delete/" + 100))
                .andExpect(status().is3xxRedirection());
    }
}
