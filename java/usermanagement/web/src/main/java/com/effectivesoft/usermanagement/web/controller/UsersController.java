package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.entity.UserRole;
import com.effectivesoft.usermanagement.entity.UserSession;
import com.effectivesoft.usermanagement.service.RoleService;
import com.effectivesoft.usermanagement.service.UserRoleService;
import com.effectivesoft.usermanagement.service.UserService;
import com.effectivesoft.usermanagement.service.UserSessionService;
import com.effectivesoft.usermanagement.web.dto.Filter;
import com.effectivesoft.usermanagement.web.dto.SortOrder;
import com.effectivesoft.usermanagement.web.form.UserForm;
import com.effectivesoft.usermanagement.web.form.validator.UserFormValidator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UsersController extends AbstractController {
    private static final String USERS_MENU_ITEM = "Users";


    private static final String USERNAMES_ATTRIBUTE = "usernames";
    private static final String USER_FORM_ATTRIBUTE = "userForm";
    private static final String ROLES_ATTRIBUTE = "roles";


    private static final String FILTER_USERNAME = "filterUsername";
    private static final String FILTER_DESCRIPTION = "filterDescription";
    private static final String FILTER_EMAIL = "filterEmail";

    private static final String FILTER_LAST_LOGON = "filterLastLogon";
    private static final String FILTER_LOCKED = "filterLocked";
    private static final String FILTER_ADMIN = "filterAdmin";

    private static final String SORT_DESCRIPTION_ASK = "description,asc";
    private static final String SORT_DESCRIPTION_DESC = "description,desc";
    private static final String SORT_DESCRIPTION_PROPERTY = "description";

    private static final String SORT_EMAIL_ASK = "email,asc";
    private static final String SORT_EMAIL_DESC = "email,desc";
    private static final String SORT_EMAIL_PROPERTY = "email";

    private static final String SORT_LAST_LOGON_ASK = "lastLogon,asc";
    private static final String SORT_LAST_LOGON_DESC = "lastLogon,desc";
    private static final String SORT_LAST_LOGON_PROPERTY = "lastLogon";

    private static final String SORT_INVALID_LOGIN_COUNT_ASK = "invalidLoginCount,asc";
    private static final String SORT_INVALID_LOGIN_COUNT_DESC = "invalidLoginCount,desc";
    private static final String SORT_INVALID_LOGIN_COUNT_PROPERTY = "invalidLoginCount";

    private static final String SORT_LOCKED_ASK = "locked,asc";
    private static final String SORT_LOCKED_DESC = "locked,desc";
    private static final String SORT_LOCKED_PROPERTY = "locked";

    private static final String SORT_ADMIN_ASK = "admin,asc";
    private static final String SORT_ADMIN_DESC = "admin,desc";
    private static final String SORT_ADMIN_PROPERTY = "admin";

    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final String FILTER_DATE_FORMAT = "dd-MMM-yyyy";
    private static final Integer PASSWORD_ENCODER_STRENGTH = 256;
    private static final String DEFAULT_PASSWORD_VALUE = "defaultpassword";


    private Map<String, SortOrder> sortParamSortOrderMap;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserFormValidator userFormValidator;

    @Autowired
    private UserSessionService userSessionService;

    @PostConstruct
    private void init() {
        sortParamSortOrderMap = new HashMap<String, SortOrder>();
        sortParamSortOrderMap.put(SORT_USERNAME_ASK, new SortOrder(Sort.Direction.ASC, SORT_USERNAME_PROPERTY));
        sortParamSortOrderMap.put(SORT_USERNAME_DESC, new SortOrder(Sort.Direction.DESC, SORT_USERNAME_PROPERTY));

        sortParamSortOrderMap.put(SORT_DESCRIPTION_ASK, new SortOrder(Sort.Direction.ASC, SORT_DESCRIPTION_PROPERTY));
        sortParamSortOrderMap.put(SORT_DESCRIPTION_DESC, new SortOrder(Sort.Direction.DESC, SORT_DESCRIPTION_PROPERTY));

        sortParamSortOrderMap.put(SORT_EMAIL_ASK, new SortOrder(Sort.Direction.ASC, SORT_EMAIL_PROPERTY));
        sortParamSortOrderMap.put(SORT_EMAIL_DESC, new SortOrder(Sort.Direction.DESC, SORT_EMAIL_PROPERTY));

        sortParamSortOrderMap.put(SORT_LAST_LOGON_ASK, new SortOrder(Sort.Direction.ASC, SORT_LAST_LOGON_PROPERTY));
        sortParamSortOrderMap.put(SORT_LAST_LOGON_DESC, new SortOrder(Sort.Direction.DESC, SORT_LAST_LOGON_PROPERTY));

        sortParamSortOrderMap.put(SORT_INVALID_LOGIN_COUNT_ASK, new SortOrder(Sort.Direction.ASC, SORT_INVALID_LOGIN_COUNT_PROPERTY));
        sortParamSortOrderMap.put(SORT_INVALID_LOGIN_COUNT_DESC, new SortOrder(Sort.Direction.DESC, SORT_INVALID_LOGIN_COUNT_PROPERTY));

        sortParamSortOrderMap.put(SORT_LOCKED_ASK, new SortOrder(Sort.Direction.ASC, SORT_LOCKED_PROPERTY));
        sortParamSortOrderMap.put(SORT_LOCKED_DESC, new SortOrder(Sort.Direction.DESC, SORT_LOCKED_PROPERTY));

        sortParamSortOrderMap.put(SORT_ADMIN_ASK, new SortOrder(Sort.Direction.ASC, SORT_ADMIN_PROPERTY));
        sortParamSortOrderMap.put(SORT_ADMIN_DESC, new SortOrder(Sort.Direction.DESC, SORT_ADMIN_PROPERTY));

    }

    @RequestMapping(value = USERS_MAPPING, method = RequestMethod.GET)
    public String goToUsers(Model model,
                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER_VALUE) Integer page,
                            @RequestParam(required = false, defaultValue = SORT_USERNAME_ASK) String sort,
                            @RequestParam(required = false) String filterUsername,
                            @RequestParam(required = false) String filterDescription,
                            @RequestParam(required = false) String filterEmail,
                            @RequestParam(required = false) String filterLastLogon,
                            @RequestParam(required = false) String filterLocked,
                            @RequestParam(required = false) String filterAdmin) {
        model.addAttribute(CURRENT_PAGE_PARAM, USERS_MENU_ITEM);
        if (page < DEFAULT_PAGE_NUMBER){
            page = DEFAULT_PAGE_NUMBER;
        }
        SortOrder sortOrder = sortParamSortOrderMap.get(sort);
        if (sortOrder == null) {
            sortOrder = new SortOrder(Sort.Direction.ASC, SORT_USERNAME_PROPERTY);
        }
        model.addAttribute(SORT_ORDER_ATTRIBUTE, sortOrder);

        List<User> users = userService.findAll(new Sort(Sort.Direction.ASC, SORT_USERNAME_PROPERTY));
        List<String> usernames = new ArrayList<String>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        model.addAttribute(USERNAMES_ATTRIBUTE, usernames);

        Pageable pageable = new PageRequest(page, DEFAULT_PAGE_SIZE, new Sort(sortOrder.getDirection(), sortOrder.getProperty()));

        Page<User> usersPage;

        if (filterUsername != null && !ALL.equalsIgnoreCase(filterUsername)) {
            Filter filter = new Filter(FILTER_USERNAME, filterUsername);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            usersPage = userService.findAllByUsername(pageable, filterUsername);
        } else if (filterDescription != null && !filterDescription.isEmpty()) {
            Filter filter = new Filter(FILTER_DESCRIPTION, filterDescription);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            usersPage = userService.findAllByDescription(pageable, filterDescription);
        } else if (filterEmail != null && !filterEmail.isEmpty()) {
            Filter filter = new Filter(FILTER_EMAIL, filterEmail);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            usersPage = userService.findAllByEmail(pageable, filterEmail);
        } else if (filterLastLogon != null && !filterLastLogon.isEmpty()) {
            Filter filter = new Filter(FILTER_LAST_LOGON, filterLastLogon);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            DateFormat dateFormat = new SimpleDateFormat(FILTER_DATE_FORMAT);
            try {
                Date lastLogon = dateFormat.parse(filterLastLogon);
                DateTime lastLogonTimeStart = new DateTime(lastLogon);
                Date lastLogonStart = lastLogonTimeStart.withTimeAtStartOfDay().toDate();
                DateTime lastLogonTimeEnd = new DateTime(lastLogon);
                Date lastLogonEnd = lastLogonTimeEnd.plusDays(1).withTimeAtStartOfDay().toDate();
                model.addAttribute(FILTER_ATTRIBUTE, filter);
                usersPage = userService.findAllByLastLogonBetween(pageable, lastLogonStart, lastLogonEnd);
            } catch (ParseException e) {
                usersPage = userService.findAll(pageable);
            }
        } else if (filterLocked != null && !ALL.equalsIgnoreCase(filterLocked)) {
            Filter filter = new Filter(FILTER_LOCKED, filterLocked);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            Boolean locked = getBooleanFromYesNoFilter(filterLocked);
            if (locked != null) {
                usersPage = userService.findAllByLocked(pageable, locked);
            } else {
                usersPage = userService.findAll(pageable);
            }
        } else if (filterAdmin != null && !ALL.equalsIgnoreCase(filterAdmin)) {
            Filter filter = new Filter(FILTER_ADMIN, filterAdmin);
            model.addAttribute(FILTER_ATTRIBUTE, filter);
            Boolean admin = getBooleanFromYesNoFilter(filterAdmin);
            if (admin != null) {
                usersPage = userService.findAllByAdmin(pageable, admin);
            } else {
                usersPage = userService.findAll(pageable);
            }
        } else {
            usersPage = userService.findAll(pageable);
        }

        model.addAttribute(PAGE_ATTRIBUTE, usersPage);
        return USERS_VIEW;
    }

    @RequestMapping(value = EDIT_USER_MAPPING, method = RequestMethod.GET)
    public String edit(Model model, @RequestParam Integer id) {
        User user = userService.findOne(id);
        if(user == null){
            return REDIRECT + USERS_MAPPING;
        }
        prepareAddEditPages(model, user, null);
        return EDIT_USER_VIEW;
    }

    @RequestMapping(value = ADD_USER_MAPPING, method = RequestMethod.GET)
    public String add(Model model) {
        prepareAddEditPages(model, null, null);
        return ADD_USER_VIEW;
    }

    @RequestMapping(value = SAVE_USER_MAPPING, method = RequestMethod.POST)
    public String save(Model model,
                       @ModelAttribute(USER_FORM_ATTRIBUTE) UserForm userForm,
                       BindingResult result) {
        if (userForm == null){
            return REDIRECT + USERS_MAPPING;
        }

        List<ObjectError> errors = userFormValidator.validate(userForm);
        if (errors.size() > 0){
            for (ObjectError error : errors){
                result.addError(error);
            }
        }

        if (result.hasErrors()){
            prepareAddEditPages(model, null, userForm);
            return userForm.getId() == null ? ADD_USER_VIEW : EDIT_USER_VIEW;
        }

        User user = createUser(userForm);

        List<Role> selectedRoles = new ArrayList<Role>();
        for (Integer roleId : userForm.getRoleIds()) {
            Role attachedRole = roleService.findOne(roleId);
            selectedRoles.add(attachedRole);
        }

        userService.saveUser(user, selectedRoles);

        return REDIRECT + USERS_MAPPING;
    }

    private User createUser(UserForm userForm){
        boolean newUser = userForm.getId() == null ? true : false;

        User user = null;
        Calendar calendar = Calendar.getInstance();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!newUser) {
            user = userService.findOne(userForm.getId());
        }
        if (user == null) {
            user = new User();
            user.setCreatedAt(calendar.getTime());
            user.setCreatedBy(username);
            user.setInvalidLoginCount(0);
        }

        user.setUpdatedAt(calendar.getTime());
        user.setUpdatedBy(username);
        user.setUsername(userForm.getUsername());

        if(YES.equalsIgnoreCase(userForm.getPasswordChanged()) || newUser){
            MessageDigestPasswordEncoder passwordEncoder = new ShaPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
            String encodedPassword = passwordEncoder.encodePassword(userForm.getPassword(), userForm.getUsername());
            user.setPassword(encodedPassword);
        }

        user.setDescription(userForm.getDescription());
        user.setEmail(userForm.getEmail());
        user.setLocked(YES.equalsIgnoreCase(userForm.getLocked()));
        user.setAdmin(YES.equalsIgnoreCase(userForm.getAdmin()));

        return user;
    }

    @RequestMapping(value = CLEAR_MOBILE_SESSION_MAPPING, method = RequestMethod.GET)
    public String clearMobileSession(@RequestParam(required = true) Integer userId) {
        User user = userService.findOne(userId);
        if(user == null){
            return REDIRECT + USERS_MAPPING;
        }
        Calendar calendar = Calendar.getInstance();
        List<UserSession> userSessions = user.getUserSessions();
        for (UserSession session : userSessions){
            session.setExpiration(calendar.getTime());
        }
        userSessionService.save(userSessions);
        return REDIRECT + USERS_MAPPING;
    }

    private void prepareAddEditPages(Model model, User user, UserForm userForm){
        model.addAttribute(CURRENT_PAGE_PARAM, USERS_MENU_ITEM);

        Sort sort = new Sort(Sort.Direction.ASC, SORT_NAME_PROPERTY);
        List<Role> roles = roleService.findAll(sort);
        model.addAttribute(ROLES_ATTRIBUTE, roles);

        if (userForm == null){
            userForm = new UserForm();
            if (user != null){
                fillUserForm(userForm, user);
            } else {
                userForm.setLocked(NO);
                userForm.setAdmin(NO);
            }
        }
        model.addAttribute(USER_FORM_ATTRIBUTE, userForm);
    }

    private void fillUserForm(UserForm form, User user){
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setPassword(DEFAULT_PASSWORD_VALUE);
        form.setDescription(user.getDescription());
        form.setEmail(user.getEmail());
        String locked = user.getLocked() ? YES : NO;
        form.setLocked(locked);
        String admin = user.getAdmin() ? YES : NO;
        form.setAdmin(admin);
        List<UserRole> userRoles = user.getUserRoles();
        Integer [] roleIds = new Integer[userRoles.size()];
        int i = 0;
        for (UserRole userRole : userRoles){
            roleIds[i] = userRole.getRole().getId();
            i++;
        }
        form.setRoleIds(roleIds);
    }

}
