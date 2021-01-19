package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.Product;
import com.effectivesoft.usermanagement.entity.ProductName;
import com.effectivesoft.usermanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
public class TargetsController extends AbstractController {
    private static final String TARGETS_MENU_ITEM = "Targets";

    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String OWLS_PRODUCTS_ATTRIBUTE = "owlsProducts";

    @Autowired
    private ProductService productService;

    @RequestMapping(value = TARGETS_MAPPING, method = RequestMethod.GET)
    public String goToTargets(Model model){
        model.addAttribute(CURRENT_PAGE_PARAM, TARGETS_MENU_ITEM);

        List<Product> products = productService.findAll();
        model.addAttribute(PRODUCTS_ATTRIBUTE, products);

        List<ProductName> productNames = ProductName.toArrayList();
        model.addAttribute(OWLS_PRODUCTS_ATTRIBUTE, productNames);
        return TARGETS_VIEW;
    }
}
