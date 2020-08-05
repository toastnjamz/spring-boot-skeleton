package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {SpringSecurityTestConfig.class})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    @WithUserDetails("regularUser")
    public void home_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("curvePointList"));
    }

    @Test
    @WithUserDetails("regularUser")
    public void addCurvePointForm_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void validate_validCurvePoint_statusIsSuccessful() throws Exception {

        CurvePoint curve = new CurvePoint(1, 1.0, 1.0);
        curve.setId(1);

        mockMvc.perform(post("/curvePoint/validate")
                .param("curveId", curve.getCurveId().toString())
                .param("term", curve.getTerm().toString())
                .param("value", curve.getValue().toString()));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void showUpdateForm_validCurvePoint_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/curvePoint/update/"+1))
                .andExpect(status().is2xxSuccessful());
    }

//    @Test
//    @WithUserDetails("regularUser")
//    public void updateBid_validUpdatedBid_statusIsRedirection() throws Exception {
//        mockMvc.perform(post("/bidList/update/" + 1)
//                .param("account", "updatedAccount")
//                .param("type", "updatedType")
//                .param("bidQuantity", "10.0"))
//                .andExpect(status().is3xxRedirection());
//
//        mockMvc.perform(get("/bidList/list"))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().attributeExists("bidList"))
//                .andExpect(model().size(1));
//    }
//
//    @Test
//    @WithUserDetails("regularUser")
//    public void deleteBid_validBid_statusIsSuccessful() throws Exception {
//        mockMvc.perform(get("/bidList/delete/" + 1))
//                .andExpect(status().is2xxSuccessful());
//    }
}

