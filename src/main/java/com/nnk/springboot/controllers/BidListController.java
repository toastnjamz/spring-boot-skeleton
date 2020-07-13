package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class BidListController {

    @Autowired
    private BidListService bidListService;

    @GetMapping("/bidList/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("bidList", bidListService.findAllBids());
        mav.setViewName("bidList/list");
        return mav;
    }

    @GetMapping("/bidList/add")
    public ModelAndView addBidForm(BidList bid) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("bidList/add");
        return mav;
    }

//    @PostMapping("/bidList/validate")
//    public String validate(@Valid BidList bid, BindingResult result, Model model) {
//        // TODO: check data valid and save to db, after saving return bid list
//        return "bidList/add";
//    }

    @PostMapping("/bidList/validate")
    public ModelAndView validate(@Valid BidList bid, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            bidListService.createBidList(bid);
            model.addAttribute("bidList", bidListService.findAllBids());
            mav.setViewName("redirect:/bidList/list");
            return mav;
        }
        mav.setViewName("bidList/add");
        return mav;
    }

    //TODO: add validation that user != null? Show error message that user doesn't exist?
    @GetMapping("/bidList/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id add to model then show to the form
        ModelAndView mav = new ModelAndView();
        BidList bidList = bidListService.findById(id);
        if (bidList != null) {
            model.addAttribute("bid", bidList);
            mav.setViewName("bidList/update");
            return mav;
        }
        return mav;
    }

    @PostMapping("/bidList/update/{id}")
    public ModelAndView updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                         BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("bidList/update");
            return mav;
        }
        bidListService.updateBidList(bidList);
        mav.setViewName("redirect:/bidList/list");
        return mav;
    }

    @GetMapping("/bidList/delete/{id}")
    public ModelAndView deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        ModelAndView mav = new ModelAndView();
        bidListService.deleteBidList(id);
        mav.setViewName("redirect:/bidList/list");
        return mav;
    }
}
