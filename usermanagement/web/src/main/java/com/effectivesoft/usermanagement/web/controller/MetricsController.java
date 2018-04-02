package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.MetricsLookup;
import com.effectivesoft.usermanagement.service.MetricsLookupService;
import com.effectivesoft.usermanagement.web.dto.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MetricsController extends AbstractController {
    private static final String METRICS_MENU_ITEM = "Metrics";
    private static final Integer DEFAULT_PAGE_SIZE = 20;

    @Autowired
    MetricsLookupService metricsLookupService;

    @RequestMapping(value = METRICS_MAPPING, method = RequestMethod.GET)
    public String goToTargets(Model model,
                              @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER_VALUE) Integer page,
                              @RequestParam(required = false, defaultValue = SORT_NAME_ASK) String sort){
        model.addAttribute(CURRENT_PAGE_PARAM, METRICS_MENU_ITEM);
        if (page < DEFAULT_PAGE_NUMBER) {
            page = DEFAULT_PAGE_NUMBER;
        }

        //TODO - should depend on sort param
        SortOrder sortOrder = new SortOrder(Sort.Direction.ASC, SORT_NAME_PROPERTY);

        model.addAttribute(SORT_ORDER_ATTRIBUTE, sortOrder);
        Pageable pageable = new PageRequest(page, DEFAULT_PAGE_SIZE, new Sort(sortOrder.getDirection(), sortOrder.getProperty()));
        Page<MetricsLookup> metricsPage = metricsLookupService.findAll(pageable);
        model.addAttribute(PAGE_ATTRIBUTE, metricsPage);
        return METRICS_VIEW;
    }
}
