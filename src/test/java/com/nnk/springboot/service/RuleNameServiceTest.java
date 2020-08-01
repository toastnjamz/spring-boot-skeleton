package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RuleNameServiceTest {

    @Mock
    RuleNameRepository ruleNameRepositoryMock;

    @InjectMocks
    RuleNameService ruleNameService;

    private RuleName testRuleName;

    @Before
    public void setup() {
        testRuleName = new RuleName("name", "description", "json", "template", "sqlStr", "sqlPart");
        testRuleName.setId(1);
    }

    @Test
    public void findAllRuleNames_ruleNameExists_ruleNamesReturned() {
        // arrange
        List<RuleName> ruleNames = new ArrayList<>();
        ruleNames.add(testRuleName);

        when(ruleNameRepositoryMock.findAll()).thenReturn(ruleNames);

        // act
        List<RuleName> listResult = ruleNameService.findAllRuleNames();

        // assert
        assertTrue(listResult.size() > 0);
    }

    @Test
    public void findAllRuleNames_noRuleNameExists_noRuleNamesReturned() {
        // arrange

        // act
        List<RuleName> listResult = ruleNameService.findAllRuleNames();

        // assert
        assertTrue(listResult.size() == 0);
    }

    @Test
    public void findById_ruleNameExists_ruleNameReturned() {
        // arrange
        when(ruleNameRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(testRuleName));

        // act
        RuleName ruleNameResult = ruleNameService.findById(1);

        // assert
        assertEquals(testRuleName.getId(), ruleNameResult.getId(), 0);
    }

    @Test
    public void findById_ruleNameDoesNotExist_nullReturned() {
        // arrange

        // act
        RuleName ruleNameResult = ruleNameService.findById(2);

        // assert
        assertNull(ruleNameResult);
    }

    @Test
    public void createRuleName_validRuleName_ruleNameReturned() {
        // arrange
        RuleName testRuleName2 = new RuleName("name", "description", "json", "template", "sqlStr", "sqlPart");
        testRuleName2.setId(2);

        when(ruleNameRepositoryMock.save(testRuleName2)).thenReturn(testRuleName2);

        // act
        RuleName ruleNameResult = ruleNameService.createRuleName(testRuleName2);

        // assert
        verify(ruleNameRepositoryMock, times(1)).save(any(RuleName.class));
        assertEquals(testRuleName2.getId(), ruleNameResult.getId(), 0);
    }

    @Test
    public void updateRuleName_validRuleName_ruleNameSaved() {
        // arrange
        RuleName testRuleName2 = new RuleName("name", "description", "json", "template", "sqlStr", "sqlPart");
        testRuleName2.setId(2);
        ruleNameService.createRuleName(testRuleName2);
        testRuleName2.setName("updatedName");

        // act
        ruleNameService.updateRuleName(testRuleName2);

        // assert
        verify(ruleNameRepositoryMock, times(2)).save(any(RuleName.class));
        assertEquals(testRuleName2.getName(), "updatedName", "updatedName");
    }

    @Test
    public void deleteRuleName_validRuleName_ruleNameDeleted() {
        // arrange

        // act
        ruleNameService.deleteRuleName(1);

        // assert
        Optional<RuleName> ruleName = ruleNameRepositoryMock.findById(1);
        assertFalse(ruleName.isPresent());
    }
}
