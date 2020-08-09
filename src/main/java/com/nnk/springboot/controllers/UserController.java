package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
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
@Api(description="CRUD operations pertaining to User")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    /**
     * Loads all Users
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/user/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userService.findAll());
        mav.setViewName("user/list");
        log.info("GET request received for home()");
        return mav;
    }

    /**
     * Loads User add form
     * @param bid
     * @return ModelAndView
     */
    @GetMapping("/user/add")
    public ModelAndView addUser(User bid) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/add");
        log.info("GET request received for addUser()");
        return mav;
    }

    /**
     * Validates and creates a new User
     * @param user
     * @param result
     * @param model
     * @return ModelAndView
     */
    @PostMapping("/user/validate")
    public ModelAndView validate(@Valid User user, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!(user.getPassword().length() > 8 && user.getPassword().length() < 30)) {
            result.rejectValue("password", "error-password","Passwords need to be between 8-30 characters.");
        }
        if (!result.hasErrors()) {
            userService.createUser(user);
            model.addAttribute("users", userService.findAll());
            mav.setViewName("redirect:/user/list");
            log.info("Add User " + user.toString());
            return mav;
        }
        mav.setViewName("user/add");
        return mav;
    }

    /**
     * Navigates to the update form for the requested User
     * @param id
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/user/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findById(id);
        if (user != null) {
            user.setPassword("");
            model.addAttribute("user", user);
            mav.setViewName("user/update");
            log.info("GET request received for showUpdateForm()");
            return mav;
        }
        return mav;
    }

    /**
     * Validates and updates the requested User
     * @param id
     * @param user
     * @param result
     * @param model
     * @return ModelAndView
     */
    @PostMapping("/user/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("user/update");
            return mav;
        }
        user.setId(id);
        userService.updateUser(user);
        model.addAttribute("users", userService.findAll());
        mav.setViewName("redirect:/user/list");
        log.info("Update User " + user.toString());
        return mav;
    }

    /**
     * Deletes the requested User
     * @param id
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/user/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findById(id);
        if (user != null) {
            userService.deleteUser(id);
            model.addAttribute("users", userService.findAll());
            mav.setViewName("redirect:/user/list");
            log.info("Delete User " + user.toString());
            return mav;
        }
        return mav;
    }
}
