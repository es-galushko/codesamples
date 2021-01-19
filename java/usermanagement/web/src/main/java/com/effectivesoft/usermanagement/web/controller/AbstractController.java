package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.Product;
import com.effectivesoft.usermanagement.entity.ProductName;
import com.effectivesoft.usermanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public abstract class AbstractController {
    protected static final String LOGIN_VIEW = "login";
    protected static final String DASHBOARD_VIEW = "dashboard";
    protected static final String ROLES_VIEW = "roles";
    protected static final String AUDIT_VIEW = "audit";
    protected static final String ALERTS_VIEW = "alerts";
    protected static final String USERS_VIEW = "users";
    protected static final String ADD_USER_VIEW = "adduser";
    protected static final String EDIT_USER_VIEW = "edituser";
    protected static final String EDIT_ROLE_VIEW = "editrole";
    protected static final String ADD_ROLE_VIEW = "addrole";
    protected static final String EDIT_ALERT_VIEW = "editalert";
    protected static final String ADD_ALERT_VIEW = "addalert";
    protected static final String METRICS_VIEW = "metrics";
    protected static final String TARGETS_VIEW = "targets";

    protected static final String DASHBOARD_MAPPING = "dashboard.html";
    protected static final String LOGIN_MAPPING = "login.html";
    protected static final String AUDIT_MAPPING = "audit.html";
    protected static final String ROLES_MAPPING = "roles.html";
    protected static final String ADD_ROLE_MAPPING = "add-role.html";
    protected static final String EDIT_ROLE_MAPPING = "edit-role.html";
    protected static final String SAVE_ROLE_MAPPING = "save-role.html";
    protected static final String DELETE_ROLE_MAPPING = "delete-role.html";
    protected static final String USERS_MAPPING = "users.html";
    protected static final String ADD_USER_MAPPING = "add-user.html";
    protected static final String EDIT_USER_MAPPING = "edit-user.html";
    protected static final String SAVE_USER_MAPPING = "save-user.html";
    protected static final String CLEAR_MOBILE_SESSION_MAPPING = "clear-mobile-session.html";
    protected static final String ALERTS_MAPPING = "alerts.html";
    protected static final String SWITCH_ALERT_MAPPING = "switch-alert.html";
    protected static final String DELETE_ALERT_MAPPING = "delete-alert.html";
    protected static final String EDIT_ALERT_MAPPING = "edit-alert.html";
    protected static final String ADD_ALERT_MAPPING = "add-alert.html";
    protected static final String SAVE_ALERT_MAPPING = "save-alert.html";
    protected static final String GET_METRICS_MAPPING = "get-metrics.json";
    protected static final String GET_ALERT_OBJECT_TYPES_MAPPING = "get-alert-object-types.json";
    protected static final String TARGETS_MAPPING = "targets.html";
    protected static final String METRICS_MAPPING = "metrics.html";

    protected static final String SORT_NAME_PROPERTY = "name";
    protected static final String SORT_NAME_ASK = "name,asc";
    protected static final String SORT_NAME_DESC = "name,desc";

    protected static final String SORT_USERNAME_ASK = "username,asc";
    protected static final String SORT_USERNAME_DESC = "username,desc";
    protected static final String SORT_USERNAME_PROPERTY = "username";

    protected static final String CURRENT_PAGE_PARAM = "currentPage";
    protected static final String PAGE_ATTRIBUTE = "page";
    protected static final String REDIRECT = "redirect:";
    protected static final String SORT_ORDER_ATTRIBUTE = "sortOrder";
    protected static final String FILTER_ATTRIBUTE = "filter";
    protected static final String ERROR_LOGIN_PARAM = "error";

    protected static final Integer DEFAULT_PAGE_NUMBER = 0;
    protected static final String DEFAULT_PAGE_NUMBER_VALUE = "0";
    protected static final String YES = "Y";
    protected static final String NO = "N";
    protected static final String ALL = "ALL";

    protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Autowired
    private ProductService productService;

    protected List<Product> getAvailableProducts() {
        List<Product> products = new ArrayList<Product>();
        for (ProductName productName : ProductName.toArrayList()) {
            String name = productName.getName();
            Product product = productService.findByName(name);
            if (product != null) {
                products.add(product);
            }
        }
        return products;
    }

    protected Boolean getBooleanFromYesNoFilter(String filter){
        Boolean result = null;
        if (YES.equalsIgnoreCase(filter)) {
            result = true;
        } else if (NO.equalsIgnoreCase(filter)) {
            result = false;
        }
        return result;
    }
}
