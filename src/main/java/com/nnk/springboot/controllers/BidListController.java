package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class BidListController {

    @Autowired
    private BidListService bidListService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    @GetMapping("/bidList/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("bidList", bidListService.findAllBids());
        mav.setViewName("bidList/list");
        log.info("GET request received for home()");
        return mav;
    }

    @GetMapping("/bidList/add")
    public ModelAndView addBidForm(BidList bid) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("bidList/add");
        log.info("GET request received for addBidForm()");
        return mav;
    }

    @PostMapping("/bidList/validate")
    public ModelAndView validate(@Valid BidList bid, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            bidListService.createBidList(bid);
            model.addAttribute("bidList", bidListService.findAllBids());
            mav.setViewName("redirect:/bidList/list");
            log.info("Add BidList " + bid.toString());
            return mav;
        }
        mav.setViewName("bidList/add");
        return mav;
    }

    @GetMapping("/bidList/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        BidList bidList = bidListService.findById(id);
        if (bidList != null) {
            model.addAttribute("bidList", bidList);
            mav.setViewName("bidList/update");
            log.info("GET request received for showUpdateForm()");
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
        bidList.setBidListId(id);
        bidListService.updateBidList(bidList);
        model.addAttribute("bidList", bidListService.findAllBids());
        mav.setViewName("redirect:/bidList/list");
        log.info("Update BidList " + bidList.toString());
        return mav;
    }

    @GetMapping("/bidList/delete/{id}")
    public ModelAndView deleteBid(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        BidList bidList = bidListService.findById(id);
        if (bidList != null) {
            bidListService.deleteBidList(id);
            model.addAttribute("bidList", bidListService.findAllBids());
            mav.setViewName("redirect:/bidList/list");
            log.info("Delete BidList " + bidList.toString());
            return mav;
        }
        return mav;
    }
}
