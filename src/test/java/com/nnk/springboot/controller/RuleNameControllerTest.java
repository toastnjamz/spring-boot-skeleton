package com.nnk.springboot.controller;

import com.nnk.springboot.config.SpringSecurityTestConfig;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    RuleNameService ruleNameServiceMock;

    private RuleName testRuleName;

    @Before
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        testRuleName = new RuleName("testName", "testDescription", "testJSON",
                "testTemplate", "testSQLStr", "testSQLPart");
        testRuleName.setId(100);
    }

    @Test
    @WithUserDetails("regularUser")
    public void home_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ruleNameList"));
    }

    @Test
    @WithUserDetails("regularUser")
    public void addRuleForm_validUser_statusIsSuccessful() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void validate_validRuleName_statusIsSuccessful() throws Exception {
        when(ruleNameServiceMock.createRuleName(testRuleName)).thenReturn(testRuleName);

        mockMvc.perform(post("/ruleName/validate")
                .param("name", testRuleName.getName())
                .param("description", testRuleName.getDescription())
                .param("json", testRuleName.getJson())
                .param("template", testRuleName.getTemplate())
                .param("sqlStr", testRuleName.getSqlStr())
                .param("sqlPart", testRuleName.getSqlPart()));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ruleNameList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void showUpdateForm_validRuleName_statusIsSuccessful() throws Exception {
        when(ruleNameServiceMock.findById(100)).thenReturn(testRuleName);

        mockMvc.perform(get("/ruleName/update/" +100))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("regularUser")
    public void updateRuleName_validUpdatedRuleName_statusIsRedirection() throws Exception {
        when(ruleNameServiceMock.findById(100)).thenReturn(testRuleName);

        mockMvc.perform(post("/ruleName/update/" + 100)
                .param("name", "updatedName")
                .param("description", "updatedDescription")
                .param("json", "updatedJSON")
                .param("template", "updatedTemplate")
                .param("sqlStr", "updatedSQLStr")
                .param("sqlPart", "updatedSQLPart"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ruleNameList"))
                .andExpect(model().size(1));
    }

    @Test
    @WithUserDetails("regularUser")
    public void deleteRuleName_validRuleName_statusIsRedirection() throws Exception {
        when(ruleNameServiceMock.findById(100)).thenReturn(testRuleName);

        mockMvc.perform(get("/ruleName/delete/" + 100))
                .andExpect(status().is3xxRedirection());
    }
}
