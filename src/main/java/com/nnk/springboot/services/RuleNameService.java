package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Basic CRUD operations for RuleName
 * Business logic layer, separates repository from controller
 */
@Service
public class RuleNameService {

    private RuleNameRepository ruleNameRepository;

    @Autowired
    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    public List<RuleName> findAllRuleNames() {
        return ruleNameRepository.findAll();
    }

    public RuleName findById(Integer id) {
        Optional<RuleName> ruleNameOptional = ruleNameRepository.findById(id);
        if (ruleNameOptional.isPresent()) {
            RuleName ruleName = ruleNameOptional.get();
            return ruleName;
        }
        return null;
    }

    public RuleName createRuleName(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public void updateRuleName(RuleName ruleName) {
        if (ruleNameRepository.findById(ruleName.getId()) != null) {
            ruleNameRepository.save(ruleName);
        }
    }

    public void deleteRuleName(Integer id) {
        if (ruleNameRepository.findById(id) != null) {
            ruleNameRepository.deleteById(id);
        }
    }
}
