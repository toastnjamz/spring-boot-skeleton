package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@Api(description="CRUD operations pertaining to RuleName")
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    /**
     * Loads all RuleNames
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/ruleName/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("ruleNameList", ruleNameService.findAllRuleNames());
        mav.setViewName("ruleName/list");
        log.info("GET request received for home()");
        return mav;
    }

    /**
     * Loads RuleName add form
     * @param ruleName
     * @return ModelAndView
     */
    @GetMapping("/ruleName/add")
    public ModelAndView addRuleForm(RuleName ruleName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ruleName/add");
        log.info("GET request received for addRuleForm()");
        return mav;
    }

    /**
     * Validates and creates a new RuleName
     * @param ruleName
     * @param result
     * @param model
     * @return ModelAndView
     */
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

    /**
     * Navigates to the update form for the requested RuleName
     * @param id
     * @param model
     * @return ModelAndView
     */
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

    /**
     * Validates and updates the requested RuleName
     * @param id
     * @param ruleName
     * @param result
     * @param model
     * @return ModelAndView
     */
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

    /**
     * Deletes the requested RuleName
     * @param id
     * @param model
     * @return ModelAndView
     */
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
