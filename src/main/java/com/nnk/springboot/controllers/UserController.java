package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
//    private UserRepository userRepository;
    private UserService userService;

    @GetMapping("/user/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userService.findAll());
//        model.addAttribute("users", userService.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    @GetMapping("/user/add")
    public ModelAndView addUser(User bid) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/add");
        return mav;
    }

//    @PostMapping("/user/validate")
//    public String validate(@Valid User user, BindingResult result, Model model) {
//        if (!result.hasErrors()) {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            user.setPassword(encoder.encode(user.getPassword()));
//            userRepository.save(user);
//            model.addAttribute("users", userRepository.findAll());
//            return "redirect:/user/list";
//        }
//        return "user/add";
//    }

    @PostMapping("/user/validate")
    public ModelAndView validate(@Valid User user, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.createUser(user);
//            userRepository.save(user);
            model.addAttribute("users", userService.findAll());
            mav.setViewName("redirect:/user/list");
            return mav;
        }
        mav.setViewName("user/add");
        return mav;
    }

    //TODO: add validation that user != null? Show error message that user doesn't exist?
    @GetMapping("/user/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        User user = userService.findById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        mav.setViewName("user/update");
        return mav;
    }

    @PostMapping("/user/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("user/update");
            return mav;
        }
        //TODO: maybe put this logic in the service layer?
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.updateUser(id);
//        userRepository.save(user);
        model.addAttribute("users", userService.findAll());
        mav.setViewName("redirect:/user/list");
        return mav;
    }

    //TODO: Add error message if user doesn't exist?
    @GetMapping("/user/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//        userRepository.delete(user);
        User user = userService.findById(id);
        userService.deleteUser(user);
        model.addAttribute("users", userService.findAll());
        mav.setViewName("redirect:/user/list");
        return mav;
    }
}
