package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    @GetMapping("/curvePoint/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("curvePointList", curvePointService.findAllCurvePoints());
        mav.setViewName("curvePoint/list");
        log.info("GET request received for home()");
        return mav;
    }

    @GetMapping("/curvePoint/add")
    public ModelAndView addCurvePointForm(CurvePoint curvePoint) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("curvePoint/add");
        log.info("GET request received for addCurvePointForm()");
        return mav;
    }

    @PostMapping("/curvePoint/validate")
    public ModelAndView validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            curvePointService.createCurvePoint(curvePoint);
            model.addAttribute("curvePointList", curvePointService.findAllCurvePoints());
            mav.setViewName("redirect:/curvePoint/list");
            log.info("Add CurvePoint " + curvePoint.toString());
            return mav;
        }
        mav.setViewName("curvePoint/add");
        return mav;
    }

    @GetMapping("/curvePoint/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        CurvePoint curvePoint = curvePointService.findById(id);
        if (curvePoint != null) {
            model.addAttribute("curvePoint", curvePoint);
            mav.setViewName("curvePoint/update");
            log.info("GET request received for showUpdateForm()");
            return mav;
        }
        return mav;
    }

    @PostMapping("/curvePoint/update/{id}")
    public ModelAndView updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                  BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("curvePoint/update");
            return mav;
        }
        curvePoint.setCurveId(id);
        curvePointService.updateCurvePoint(curvePoint);
        model.addAttribute("curvePointList", curvePointService.findAllCurvePoints());
        mav.setViewName("redirect:/curvePoint/list");
        log.info("Update CurvePoint " + curvePoint.toString());
        return mav;
    }

    @GetMapping("/curvePoint/delete/{id}")
    public ModelAndView deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        CurvePoint curvePoint = curvePointService.findById(id);
        if (curvePoint != null) {
            curvePointService.deleteCurvePoint(id);
            model.addAttribute("curvePointList", curvePointService.findAllCurvePoints());
            mav.setViewName("redirect:/curvePoint/list");
            log.info("Delete CurvePoint " + curvePoint.toString());
            return mav;
        }
        return mav;
    }
}
