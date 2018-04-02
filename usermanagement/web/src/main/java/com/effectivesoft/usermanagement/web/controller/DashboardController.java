package com.effectivesoft.usermanagement.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DashboardController extends AbstractController {

    private static final String DASHBOARD_MENU_ITEM = "Home";

    @RequestMapping(value = DASHBOARD_MAPPING, method = RequestMethod.GET)
    public String goToDashboard(Model model) {
        model.addAttribute(CURRENT_PAGE_PARAM, DASHBOARD_MENU_ITEM);
        return DASHBOARD_VIEW;
    }
}
