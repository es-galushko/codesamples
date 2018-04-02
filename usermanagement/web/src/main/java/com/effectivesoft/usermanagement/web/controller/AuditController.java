package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.Audit;
import com.effectivesoft.usermanagement.entity.AuditEventType;
import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.service.AuditService;
import com.effectivesoft.usermanagement.service.UserService;
import com.effectivesoft.usermanagement.web.dto.Filter;
import com.effectivesoft.usermanagement.web.dto.SortOrder;
import org.joda.time.DateTime;
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

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class AuditController extends AbstractController {

    private static final String AUDIT_MENU_ITEM = "Audit Logs";
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_ATTRIBUTE = "username,asc";
    private static final String USERNAMES_ATTRIBUTE = "usernames";
    private static final String EVENT_NAMES_ATTRIBUTE = "eventNames";
    private static final String FILTER_DATE_FORMAT = "yyyy-MM-dd";

    private static final String FILTER_USERNAME = "filterUsername";
    private static final String FILTER_EVENT_NAME = "filterEventName";
    private static final String FILTER_EVENT_DESCRIPTION = "filterEventDescription";
    private static final String FILTER_DATE = "filterDate";

    private static final String ALL_VALUE = "all";

    private static final String SORT_EVENT_NAME_ASK = "invalidLoginCount,asc";
    private static final String SORT_EVENT_NAME_DESK = "invalidLoginCount,asc";
    private static final String SORT_EVENT_NAME_PROPERTY = "eventName";

    private static final String SORT_EVENT_DESCRIPTION_ASK = "invalidLoginCount,asc";
    private static final String SORT_EVENT_DESCRIPTION_DESK = "invalidLoginCount,asc";
    private static final String SORT_EVENT_DESCRIPTION_PROPERTY = "eventDescription";

    private static final String SORT_CREATED_AT_ASK = "invalidLoginCount,asc";
    private static final String SORT_CREATED_AT_DESK = "invalidLoginCount,asc";
    private static final String SORT_CREATED_AT_PROPERTY = "createdAt";

    private Map<String, SortOrder> sortParamSortOrderMap;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @PostConstruct
    private void init() {
        sortParamSortOrderMap = new HashMap<String, SortOrder>();
        sortParamSortOrderMap.put(SORT_USERNAME_ASK, new SortOrder(Sort.Direction.ASC, SORT_USERNAME_PROPERTY));
        sortParamSortOrderMap.put(SORT_USERNAME_DESC, new SortOrder(Sort.Direction.DESC, SORT_USERNAME_PROPERTY));

        sortParamSortOrderMap.put(SORT_EVENT_NAME_ASK, new SortOrder(Sort.Direction.ASC, SORT_EVENT_NAME_PROPERTY));
        sortParamSortOrderMap.put(SORT_EVENT_NAME_DESK, new SortOrder(Sort.Direction.DESC, SORT_EVENT_NAME_PROPERTY));

        sortParamSortOrderMap.put(SORT_EVENT_DESCRIPTION_ASK, new SortOrder(Sort.Direction.ASC, SORT_EVENT_DESCRIPTION_PROPERTY));
        sortParamSortOrderMap.put(SORT_EVENT_DESCRIPTION_DESK, new SortOrder(Sort.Direction.DESC, SORT_EVENT_DESCRIPTION_PROPERTY));

        sortParamSortOrderMap.put(SORT_CREATED_AT_ASK, new SortOrder(Sort.Direction.ASC, SORT_CREATED_AT_PROPERTY));
        sortParamSortOrderMap.put(SORT_CREATED_AT_DESK, new SortOrder(Sort.Direction.DESC, SORT_CREATED_AT_PROPERTY));
    }

    @RequestMapping(value = AUDIT_MAPPING, method = RequestMethod.GET)
    public String goToAudit(Model model,
                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER_VALUE) Integer page,
                            @RequestParam(required = false, defaultValue = DEFAULT_SORT_ATTRIBUTE) String sort,
                            @RequestParam(required = false) String filterUsername,
                            @RequestParam(required = false) String filterEventName,
                            @RequestParam(required = false) String filterEventDescription,
                            @RequestParam(required = false) String filterDate) {
        model.addAttribute(CURRENT_PAGE_PARAM, AUDIT_MENU_ITEM);
        if (page < DEFAULT_PAGE_NUMBER){
            page = DEFAULT_PAGE_NUMBER;
        }

        List<User> users = userService.findAll();
        List<String> usernames = new ArrayList<String>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        model.addAttribute(USERNAMES_ATTRIBUTE, usernames);

        List<String> eventNames = new ArrayList<String>();
        for (AuditEventType auditEventType : AuditEventType.values()) {
            eventNames.add(auditEventType.getEventName());
        }
        model.addAttribute(EVENT_NAMES_ATTRIBUTE, eventNames);

        SortOrder sortOrder = sortParamSortOrderMap.get(sort);
        if (sortOrder == null) {
            sortOrder = new SortOrder(Sort.Direction.ASC, SORT_USERNAME_PROPERTY);
        }
        model.addAttribute(SORT_ORDER_ATTRIBUTE, sortOrder);

        Pageable pageable = new PageRequest(page, DEFAULT_PAGE_SIZE, new Sort(sortOrder.getDirection(), sortOrder.getProperty()));

        Page<Audit> auditPage;

        if (filterUsername != null && !filterUsername.equals(ALL_VALUE)) {
            Filter filter = new Filter(FILTER_USERNAME, filterUsername);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            auditPage = auditService.findAllByUsername(pageable, filterUsername);
        } else if (filterEventName != null && !filterEventName.equals(ALL_VALUE)) {
            Filter filter = new Filter(FILTER_EVENT_NAME, filterEventName);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            auditPage = auditService.findAllByEventName(pageable, filterEventName);
        } else if (filterEventDescription != null && !filterEventDescription.trim().isEmpty()) {
            Filter filter = new Filter(FILTER_EVENT_DESCRIPTION, filterEventDescription);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            auditPage = auditService.findAllByEventDescription(pageable, filterEventDescription);
        } else if (filterDate != null && !filterDate.trim().isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat(FILTER_DATE_FORMAT);
            try {
                Date createdAt = dateFormat.parse(filterDate);

                DateTime createdDateTimeStart = new DateTime(createdAt);
                Date createdAtStart = createdDateTimeStart.withTimeAtStartOfDay().toDate();

                DateTime createdDateTimeEnd = new DateTime(createdAt);
                Date createdAtEnd = createdDateTimeEnd.plusDays(1).withTimeAtStartOfDay().toDate();

                Filter filter = new Filter(FILTER_DATE, filterDate);
                model.addAttribute(FILTER_ATTRIBUTE, filter);


                auditPage = auditService.findAllByCreatedAt(pageable, createdAtStart, createdAtEnd);
            } catch (ParseException e) {
                auditPage = auditService.findAll(pageable);
            }
        } else {
            auditPage = auditService.findAll(pageable);
        }

        model.addAttribute(PAGE_ATTRIBUTE, auditPage);


        return AUDIT_VIEW;
    }
}
