package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController
{
//	@RequestMapping("/")
//	public String home(Model model)
//	{
//		return "home";
//	}

	@GetMapping ("/")
	public ModelAndView home(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		return modelAndView;
	}

//	@RequestMapping("/admin/home")
//	public String adminHome(Model model)
//	{
//		return "redirect:/bidList/list";
//	}

	@GetMapping("/admin/home")
	public ModelAndView adminHome(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/bidList/list");
		return modelAndView;
	}
}
