package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest(classes = {SpringSecurityTestConfig.class})
@RunWith(SpringRunner.class)
public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    private BidListService bidListServiceMock;

    private BidList testBid;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        testBid = new BidList("testAccount", "testType", 1.0);
        testBid.setBidListId(100);
    }

    @Test
    @WithUserDetails("regularUser")
    public void home_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithUserDetails("regularUser")
    public void addBidForm_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void validate_validBid_statusIsSuccessful() throws Exception {
        when(bidListServiceMock.createBidList(testBid)).thenReturn(testBid);

        mockMvc.perform(post("/bidList/validate")
                .param("account", testBid.getAccount())
                .param("type", testBid.getType())
                .param("bidQuantity", testBid.getBidQuantity().toString()));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void showUpdateForm_validBid_statusIsSuccessful() throws Exception {
        when(bidListServiceMock.findById(100)).thenReturn(testBid);

        mockMvc.perform(get("/bidList/update/" +100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void updateBid_validUpdatedBid_statusIsRedirection() throws Exception {
        when(bidListServiceMock.findById(100)).thenReturn(testBid);

        mockMvc.perform(post("/bidList/update/" + 100)
                .param("account", "updatedAccount")
                .param("type", "updatedType")
                .param("bidQuantity", "10.0"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void deleteBid_validBid_statusIsRedirection() throws Exception {
        when(bidListServiceMock.findById(100)).thenReturn(testBid);

        mockMvc.perform(get("/bidList/delete/" + 100))
                .andExpect(status().is3xxRedirection());
    }
}
