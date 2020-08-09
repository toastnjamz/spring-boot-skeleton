package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    RatingService ratingServiceMock;

    private Rating testRating;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        testRating = new Rating("moodys", "sandp", "fitch", 1);
        testRating.setId(100);
    }

    @Test
    @WithUserDetails("regularUser")
    public void home_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ratingList"));
    }

    @Test
    @WithUserDetails("regularUser")
    public void addRatingForm_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().is2xxSuccessful());
    }

    //TODO
    @Test
    @WithUserDetails("regularUser")
    public void validate_validRating_statusIsSuccessful() throws Exception {
        when(ratingServiceMock.createRating(testRating)).thenReturn(testRating);

        mockMvc.perform(post("/rating/validate")
                .param("moodysRating", testRating.getMoodysRating())
                .param("sandPRating", testRating.getSandPRating())
                .param("fitchRating", testRating.getFitchRating())
                .param("orderNumber", testRating.getOrderNumber().toString()));

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ratingList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void showUpdateForm_validRating_statusIsSuccessful() throws Exception {
        when(ratingServiceMock.findById(100)).thenReturn(testRating);

        mockMvc.perform(get("/rating/update/" +100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void updateRating_validUpdatedRating_statusIsRedirection() throws Exception {
        when(ratingServiceMock.findById(100)).thenReturn(testRating);

        mockMvc.perform(post("/rating/update/" + 100)
                .param("moodysRating", "updatedMoodys")
                .param("sandPRating", "updatedSandP")
                .param("fitchRating", "updatedFitch")
                .param("orderNumber", "10"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ratingList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void deleteRating_validRating_statusIsRedirection() throws Exception {
        when(ratingServiceMock.findById(100)).thenReturn(testRating);

        mockMvc.perform(get("/rating/delete/" + 100))
                .andExpect(status().is3xxRedirection());
    }
}
