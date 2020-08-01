package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    @GetMapping("/ruleName/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("ruleNameList", ruleNameService.findAllRuleNames());
        mav.setViewName("ruleName/list");
        log.info("GET request received for home()");
        return mav;
    }

    @GetMapping("/ruleName/add")
    public ModelAndView addRuleForm(RuleName ruleName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ruleName/add");
        log.info("GET request received for addRuleForm()");
        return mav;
    }

    @PostMapping("/ruleName/validate")
    public ModelAndView validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            ruleNameService.createRuleName(ruleName);
            model.addAttribute("ruleNameList", ruleNameService.findAllRuleNames());
            mav.setViewName("redirect:/ruleName/list");
            log.info("Add RuleName " + ruleName.toString());
            return mav;
        }
        mav.setViewName("ruleName/add");
        return mav;
    }

    @GetMapping("/ruleName/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        RuleName ruleName = ruleNameService.findById(id);
        if (ruleName != null) {
            model.addAttribute("ruleName", ruleName);
            mav.setViewName("ruleName/update");
            log.info("GET request received for showUpdateForm()");
            return mav;
        }
        return mav;
    }

    @PostMapping("/ruleName/update/{id}")
    public ModelAndView updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                       BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("ruleName/update");
            return mav;
        }
        ruleName.setId(id);
        ruleNameService.updateRuleName(ruleName);
        model.addAttribute("ruleNameList", ruleNameService.findAllRuleNames());
        mav.setViewName("redirect:/ruleName/list");
        log.info("Update RuleName " + ruleName.toString());
        return mav;
    }

    @GetMapping("/ruleName/delete/{id}")
    public ModelAndView deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        RuleName ruleName = ruleNameService.findById(id);
        if (ruleName != null) {
            ruleNameService.deleteRuleName(id);
            model.addAttribute("ruleNameList", ruleNameService.findAllRuleNames());
            mav.setViewName("redirect:/ruleName/list");
            log.info("Delete RuleName " + ruleName.toString());
            return mav;
        }
        return mav;
    }
}
