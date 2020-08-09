package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithUserDetails;

@AutoConfigureMockMvc
@SpringBootTest(classes = {SpringSecurityTestConfig.class})
@RunWith(SpringRunner.class)
public class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    private CurvePointService curvePointServiceMock;

    private CurvePoint testCurve;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        testCurve = new CurvePoint(1, 1.0, 1.0);
        testCurve.setId(100);
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
        when(curvePointServiceMock.createCurvePoint(testCurve)).thenReturn(testCurve);

        mockMvc.perform(post("/curvePoint/validate")
                .param("curveId", testCurve.getCurveId().toString())
                .param("term", testCurve.getTerm().toString())
                .param("value", testCurve.getValue().toString()));

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("curvePointList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void showUpdateForm_validCurvePoint_statusIsSuccessful() throws Exception {
        when(curvePointServiceMock.findById(100)).thenReturn(testCurve);

        mockMvc.perform(get("/curvePoint/update/"+100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void updateCurvePoint_validUpdatedCurvePoint_statusIsRedirection() throws Exception {
        when(curvePointServiceMock.findById(100)).thenReturn(testCurve);

        mockMvc.perform(post("/curvePoint/update/" + 100)
                .param("curveId", "2")
                .param("term", "2.0")
                .param("value", "2.0"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("curvePointList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void deleteCurvePoint_validCurvePoint_statusIsRedirection() throws Exception {
        when(curvePointServiceMock.findById(100)).thenReturn(testCurve);

        mockMvc.perform(get("/curvePoint/delete/" + 100))
                .andExpect(status().is3xxRedirection());
    }
}

