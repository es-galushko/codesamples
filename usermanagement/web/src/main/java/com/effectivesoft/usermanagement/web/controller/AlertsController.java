package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.*;
import com.effectivesoft.usermanagement.service.AlertServise;
import com.effectivesoft.usermanagement.service.MetricsLookupService;
import com.effectivesoft.usermanagement.service.ProductService;
import com.effectivesoft.usermanagement.web.dto.Filter;
import com.effectivesoft.usermanagement.web.dto.SortOrder;
import com.effectivesoft.usermanagement.web.form.AlertForm;
import com.effectivesoft.usermanagement.web.form.AlertObjectTypeJson;
import com.effectivesoft.usermanagement.web.form.MetricJson;
import com.effectivesoft.usermanagement.web.form.validator.AlertFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;


@Controller
public class AlertsController extends AbstractController {
    private static final String ALERTS_MENU_ITEM = "Alert Rules";


    private static final String SORT_PRODUCT_ASK = "product.name,asc";
    private static final String SORT_PRODUCT_DESC = "product.name,desc";
    private static final String SORT_PRODUCT_PROPERTY = "product.name";

    private static final String SORT_OBJECT_TYPE_ASK = "objectType,asc";
    private static final String SORT_OBJECT_TYPE_DESC = "objectType,desc";
    private static final String SORT_OBJECT_TYPE_PROPERTY = "objectType";

    private static final String SORT_RULE_ASK = "metricsLookup.name,asc";
    private static final String SORT_RULE_DESC = "metricsLookup.name,desc";
    private static final String SORT_RULE_PROPERTY = "metricsLookup.name";

    private static final String SORT_ENABLED_ASK = "enabled,asc";
    private static final String SORT_ENABLED_DESC = "enabled,desc";
    private static final String SORT_ENABLED_PROPERTY = "enabled";

    private static final String SORT_TRIGGERED_ASK = "triggered,asc";
    private static final String SORT_TRIGGERED_DESC = "triggered,desc";
    private static final String SORT_TRIGGERED_PROPERTY = "triggered";


    private static final String FILTER_NAME = "filterName";
    private static final String FILTER_PRODUCT = "filterProduct";
    private static final String FILTER_OBJECT_TYPE = "filterObjectType";
    private static final String FILTER_RULE = "filterRule";
    private static final String FILTER_ENABLED = "filterEnabled";
    private static final String FILTER_TRIGGERED = "filterTriggered";


    private static final Integer DEFAULT_PAGE_SIZE = 50;
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String OPERATORS_ATTRIBUTE = "operators";
    private static final String ALERT_FORM_ATTRIBUTE = "alertForm";
    private static final String ALERT_OBJECT_TYPES_ATTRIBUTE = "alertObjectTypes";
    private static final String METRICS_ATTRIBUTE = "metrics";
    private static final String WHITESPACE = " ";
    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";

    @Autowired
    private AlertServise alertServise;

    @Autowired
    private AlertFormValidator alertFormValidator;

    @Autowired
    private MetricsLookupService metricsLookupService;

    @Autowired
    private ProductService productService;

    private Map<String, SortOrder> sortParamSortOrderMap;

    @PostConstruct
    private void init() {
        sortParamSortOrderMap = new HashMap<String, SortOrder>();
        sortParamSortOrderMap.put(SORT_NAME_ASK, new SortOrder(Sort.Direction.ASC, SORT_NAME_PROPERTY));
        sortParamSortOrderMap.put(SORT_NAME_DESC, new SortOrder(Sort.Direction.DESC, SORT_NAME_PROPERTY));

        sortParamSortOrderMap.put(SORT_PRODUCT_ASK, new SortOrder(Sort.Direction.ASC, SORT_PRODUCT_PROPERTY));
        sortParamSortOrderMap.put(SORT_PRODUCT_DESC, new SortOrder(Sort.Direction.DESC, SORT_PRODUCT_PROPERTY));

        sortParamSortOrderMap.put(SORT_OBJECT_TYPE_ASK, new SortOrder(Sort.Direction.ASC, SORT_OBJECT_TYPE_PROPERTY));
        sortParamSortOrderMap.put(SORT_OBJECT_TYPE_DESC, new SortOrder(Sort.Direction.DESC, SORT_OBJECT_TYPE_PROPERTY));

        sortParamSortOrderMap.put(SORT_RULE_ASK, new SortOrder(Sort.Direction.ASC, SORT_RULE_PROPERTY));
        sortParamSortOrderMap.put(SORT_RULE_DESC, new SortOrder(Sort.Direction.DESC, SORT_RULE_PROPERTY));

        sortParamSortOrderMap.put(SORT_ENABLED_ASK, new SortOrder(Sort.Direction.ASC, SORT_ENABLED_PROPERTY));
        sortParamSortOrderMap.put(SORT_ENABLED_DESC, new SortOrder(Sort.Direction.DESC, SORT_ENABLED_PROPERTY));

        sortParamSortOrderMap.put(SORT_TRIGGERED_ASK, new SortOrder(Sort.Direction.ASC, SORT_TRIGGERED_PROPERTY));
        sortParamSortOrderMap.put(SORT_TRIGGERED_DESC, new SortOrder(Sort.Direction.DESC, SORT_TRIGGERED_PROPERTY));
    }

    @RequestMapping(value = ALERTS_MAPPING, method = RequestMethod.GET)
    public String goToAlerts(Model model,
                             @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER_VALUE) Integer page,
                             @RequestParam(required = false, defaultValue = SORT_NAME_ASK) String sort,
                             @RequestParam(required = false) String filterName,
                             @RequestParam(required = false) String filterProduct,
                             @RequestParam(required = false) String filterObjectType,
                             @RequestParam(required = false) String filterRule,
                             @RequestParam(required = false) String filterEnabled,
                             @RequestParam(required = false) String filterTriggered) {
        model.addAttribute(CURRENT_PAGE_PARAM, ALERTS_MENU_ITEM);

        if (page < DEFAULT_PAGE_NUMBER) {
            page = DEFAULT_PAGE_NUMBER;
        }
        SortOrder sortOrder = sortParamSortOrderMap.get(sort);
        if (sortOrder == null) {
            sortOrder = new SortOrder(Sort.Direction.ASC, SORT_NAME_PROPERTY);
        }
        model.addAttribute(SORT_ORDER_ATTRIBUTE, sortOrder);

        List<AlertObjectType> objectTypes = AlertObjectType.toArrayList();
        model.addAttribute(ALERT_OBJECT_TYPES_ATTRIBUTE, objectTypes);

        Pageable pageable = new PageRequest(page, DEFAULT_PAGE_SIZE, new Sort(sortOrder.getDirection(), sortOrder.getProperty()));

        Page<Alert> alertPage;
        if (filterName != null && !filterName.isEmpty()) {
            Filter filter = new Filter(FILTER_NAME, filterName);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            alertPage = alertServise.findAllByName(pageable, filterName);
        } else if (filterProduct != null && !ALL.equalsIgnoreCase(filterProduct)) {
            Filter filter = new Filter(FILTER_PRODUCT, filterProduct);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            alertPage = alertServise.findAllByProduct(pageable, Integer.valueOf(filterProduct));
        } else if (filterObjectType != null && !ALL.equalsIgnoreCase(filterObjectType)) {
            Filter filter = new Filter(FILTER_OBJECT_TYPE, filterObjectType);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            alertPage = alertServise.findAllByObjectType(pageable, filterObjectType);
        } else if (filterRule != null && !filterRule.isEmpty()) {
            Filter filter = new Filter(FILTER_RULE, filterRule);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            String metric = null, operator = null, threshold = null;
            for (Operator currentOperator : Operator.values()) {
                CharSequence charSequence = currentOperator.getSymbol();
                if (filterRule.contains(charSequence)) {
                    operator = currentOperator.getSymbol();
                    break;
                }
            }
            if (operator != null){
                int indexOfOperator = filterRule.indexOf(operator);
                metric = filterRule.substring(0, indexOfOperator);
                threshold = filterRule.substring(indexOfOperator + operator.length());
                metric = removeFirstLastWhitespaces(metric);
                threshold = removeFirstLastWhitespaces(threshold);
            }
            alertPage = alertServise.findAllByRule(pageable, metric, operator, threshold, filterRule);
        } else if (filterEnabled != null && !ALL.equalsIgnoreCase(filterEnabled)) {
            Filter filter = new Filter(FILTER_ENABLED, filterEnabled);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            Boolean enabled = getBooleanFromYesNoFilter(filterEnabled);
            if (enabled != null) {
                alertPage = alertServise.findAllByEnabled(pageable, enabled);
            } else {
                alertPage = alertServise.findAll(pageable);
            }
        } else if (filterTriggered != null && !ALL.equalsIgnoreCase(filterTriggered)) {
            Filter filter = new Filter(FILTER_TRIGGERED, filterTriggered);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            Boolean triggered = getBooleanFromYesNoFilter(filterTriggered);
            if (triggered != null) {
                alertPage = alertServise.findAllByTriggered(pageable, triggered);
            } else {
                alertPage = alertServise.findAll(pageable);
            }
        } else {
            alertPage = alertServise.findAll(pageable);
        }

        model.addAttribute(PAGE_ATTRIBUTE, alertPage);

        List<Product> products = productService.findAll();
        model.addAttribute(PRODUCTS_ATTRIBUTE, products);
        return ALERTS_VIEW;
    }


    @RequestMapping(value = ADD_ALERT_MAPPING, method = RequestMethod.GET)
    public String goToAddAlert(Model model){
        prepareAddEditPages(model, null, null);
        return ADD_ALERT_VIEW;
    }

    @RequestMapping(value = EDIT_ALERT_MAPPING, method = RequestMethod.GET)
    public String goToEditAlert(Model model, @RequestParam Integer id){
        Alert alert = alertServise.findOne(id);
        if (alert == null){
            return REDIRECT + ALERTS_MAPPING;
        }
        prepareAddEditPages(model, alert, null);
        return EDIT_ALERT_VIEW;
    }

    @RequestMapping(value = SAVE_ALERT_MAPPING, method = RequestMethod.POST)
    public String save(Model model,
                       @ModelAttribute(ALERT_FORM_ATTRIBUTE) AlertForm alertForm,
                       BindingResult result){
        if (alertForm == null){
            return REDIRECT + ALERTS_MAPPING;
        }

        List<ObjectError> errors = alertFormValidator.validate(alertForm);
        if (errors.size() > 0){
            for (ObjectError error : errors){
                result.addError(error);
            }
        }

        if (result.hasErrors()){
            prepareAddEditPages(model, null, alertForm);
            return alertForm.getId() == null ? ADD_ALERT_VIEW : EDIT_ALERT_VIEW;
        }

        Alert alert = createAlert(alertForm);
        alertServise.save(alert);

        return REDIRECT + ALERTS_MAPPING;
    }

    @RequestMapping(value = DELETE_ALERT_MAPPING, method = RequestMethod.GET)
    public String delete(@RequestParam Integer id){
        Alert alert = alertServise.findOne(id);
        if (alert == null){
            return REDIRECT + ALERTS_MAPPING;
        }
        alertServise.delete(alert);
        return REDIRECT + ALERTS_MAPPING;
    }

    @RequestMapping(value = SWITCH_ALERT_MAPPING, method = RequestMethod.GET)
    public String switchAlert(@RequestParam Integer id, @RequestParam String action){
        Alert alert = alertServise.findOne(id);
        if (alert == null){
            return REDIRECT + ALERTS_MAPPING;
        }
        if (ENABLE.equalsIgnoreCase(action)){
            alert.setEnabled(true);
        }
        if (DISABLE.equals(action)){
            alert.setEnabled(false);
        }
        alertServise.save(alert);
        return REDIRECT + ALERTS_MAPPING;
    }

    @RequestMapping(value = GET_METRICS_MAPPING, method = RequestMethod.GET)
    @ResponseBody
    public List<MetricJson> getMetrics(@RequestParam Integer productId,
                                       @RequestParam String objectType){
        List<MetricJson> metricsJson = getMetricsJson(productId, objectType);
        return metricsJson;
    }

    @RequestMapping(value = GET_ALERT_OBJECT_TYPES_MAPPING, method = RequestMethod.GET)
    @ResponseBody
    public List<AlertObjectTypeJson> getAlertObjectTypes(@RequestParam Integer productId){
        List<AlertObjectTypeJson> result = new ArrayList<AlertObjectTypeJson>();
        Product product = productService.findOne(productId);
        for (ProductName productName : ProductName.toArrayList()) {
            if (productName.getName().equals(product.getName())){
                for (AlertObjectType type : AlertObjectType.toArrayList()){
                    AlertObjectTypeJson typeJson = new AlertObjectTypeJson();
                    typeJson.setName(type.getName());
                    typeJson.setDisplayName(type.getDisplayName());
                    result.add(typeJson);
                }
            }
        }
        return result;
    }

    private void prepareAddEditPages(Model model, Alert alert, AlertForm alertForm){
        model.addAttribute(CURRENT_PAGE_PARAM, ALERTS_MENU_ITEM);

        List<Operator> operators = Operator.toArrayList();
        model.addAttribute(OPERATORS_ATTRIBUTE, operators);

        List<Product> products = productService.findAll();
        model.addAttribute(PRODUCTS_ATTRIBUTE, products);

        if (alertForm == null){
            alertForm = new AlertForm();
            if (alert != null){
                fillAlertForm(alertForm, alert);
            }
        }

        if (alertForm.getProductId() != null && alertForm.getObjectType() != null && !alertForm.getObjectType().isEmpty()){
            List<AlertObjectType> objectTypes = AlertObjectType.toArrayList();
            model.addAttribute(ALERT_OBJECT_TYPES_ATTRIBUTE, objectTypes);

            List<MetricJson> metricsJson = getMetricsJson(alertForm.getProductId(), alertForm.getObjectType());
            model.addAttribute(METRICS_ATTRIBUTE, metricsJson);
        }

        model.addAttribute(ALERT_FORM_ATTRIBUTE, alertForm);
    }

    private void fillAlertForm(AlertForm alertForm, Alert alert){
        alertForm.setId(alert.getId());
        alertForm.setName(alert.getName());
        alertForm.setObjectType(alert.getObjectType());
        alertForm.setOperator(alert.getOperator());
        alertForm.setThreshold(alert.getThreshold());
        alertForm.setProductId(alert.getProduct().getId());
        alertForm.setMetricId(alert.getMetricsLookup().getId());
    }

    private Alert createAlert(AlertForm alertForm){
        Calendar calendar = Calendar.getInstance();

        Alert alert = null;
        if(alertForm.getId() != null){
            alert = alertServise.findOne(alertForm.getId());
        }
        if (alert == null){
            alert = new Alert();
            alert.setCreatedAt(calendar.getTime());
        }
        alert.setUpdatedAt(calendar.getTime());
        alert.setName(alertForm.getName());
        Product product = productService.findOne(alertForm.getProductId());
        alert.setProduct(product);
        alert.setObjectType(alertForm.getObjectType());
        MetricsLookup metricsLookup = metricsLookupService.findOne(alertForm.getMetricId());
        alert.setMetricsLookup(metricsLookup);
        alert.setOperator(alertForm.getOperator());
        alert.setThreshold(alertForm.getThreshold());
        alert.setEnabled(false);
        alert.setTriggered(false);

        return alert;
    }

    private List<MetricJson> getMetricsJson(Integer productId, String alertsObjectType){
        List<MetricsLookup> metricsLookups = metricsLookupService.findAllByProductObjectType(productId, alertsObjectType);
        List<MetricJson> metricsJson = new ArrayList<MetricJson>();
        for (MetricsLookup metricsLookup : metricsLookups){
            MetricJson metricJson = new MetricJson();
            metricJson.setId(metricsLookup.getId());
            metricJson.setName(metricsLookup.getName());
            metricsJson.add(metricJson);
        }
        return metricsJson;
    }

    private String removeFirstLastWhitespaces(String value){
        if (value.startsWith(WHITESPACE)) {
            value = value.substring(1);
        }
        if (value.endsWith(WHITESPACE)) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
