package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    private TradeService tradeServiceMock;

    private Trade testTrade;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        testTrade = new Trade("testAccount", "testType");
        testTrade.setTradeId(100);
        testTrade.setBuyQuantity(1.0);
    }

    @Test
    @WithUserDetails("regularUser")
    public void home_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("tradeList"));
    }

    @Test
    @WithUserDetails("regularUser")
    public void addTradeForm_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void validate_validTrade_statusIsSuccessful() throws Exception {
        when(tradeServiceMock.createTrade(testTrade)).thenReturn(testTrade);

        mockMvc.perform(post("/trade/validate")
                .param("account", testTrade.getAccount())
                .param("type", testTrade.getType())
                .param("buyQuantity", testTrade.getBuyQuantity().toString()));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("tradeList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void showUpdateForm_validTrade_statusIsSuccessful() throws Exception {
        when(tradeServiceMock.findById(100)).thenReturn(testTrade);

        mockMvc.perform(get("/trade/update/" +100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void updateTrade_validUpdatedTrade_statusIsSuccess() throws Exception {
        when(tradeServiceMock.findById(100)).thenReturn(testTrade);

        mockMvc.perform(post("/trade/update/" + 100)
                .param("account", "updatedAccount")
                .param("type", "updatedType")
                .param("buyQuantity", "updatedBuyQuantity"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("tradeList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void deleteTrade_validTrade_statusIsRedirection() throws Exception {
        when(tradeServiceMock.findById(100)).thenReturn(testTrade);

        mockMvc.perform(get("/trade/delete/" + 100))
                .andExpect(status().is3xxRedirection());
    }
}
