package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class TradeController {

    @Autowired
    private TradeService tradeService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    @GetMapping("/trade/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("tradeList", tradeService.findAllTrades());
        mav.setViewName("trade/list");
        log.info("GET request received for home()");
        return mav;
    }

    @GetMapping("/trade/add")
    public ModelAndView addTradeForm(Trade trade) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("trade/add");
        log.info("GET request received for addTradeForm()");
        return mav;
    }

    @PostMapping("/trade/validate")
    public ModelAndView validate(@Valid Trade trade, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            tradeService.createTrade(trade);
            model.addAttribute("tradeList", tradeService.findAllTrades());
            mav.setViewName("redirect:/trade/list");
            log.info("Add Trade " + trade.toString());
            return mav;
        }
        mav.setViewName("trade/add");
        return mav;
    }

    @GetMapping("/trade/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        Trade trade = tradeService.findById(id);
        if (trade != null) {
            model.addAttribute("trade", trade);
            mav.setViewName("trade/update");
            log.info("GET request received for showUpdateForm()");
            return mav;
        }
        return mav;
    }

    @PostMapping("/trade/update/{id}")
    public ModelAndView updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                                     BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("trade/update");
            return mav;
        }
        trade.setTradeId(id);
        tradeService.updateTrade(trade);
        model.addAttribute("tradeList", tradeService.findAllTrades());
        mav.setViewName("redirect:/trade/list");
        log.info("Update Trade " + trade.toString());
        return mav;
    }

    @GetMapping("/trade/delete/{id}")
    public ModelAndView deleteTrade(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        Trade trade = tradeService.findById(id);
        if (trade != null) {
            tradeService.deleteTrade(id);
            model.addAttribute("tradeList", tradeService.findAllTrades());
            mav.setViewName("redirect:/trade/list");
            log.info("Delete Trade " + trade.toString());
            return mav;
        }
        return mav;
    }
}
