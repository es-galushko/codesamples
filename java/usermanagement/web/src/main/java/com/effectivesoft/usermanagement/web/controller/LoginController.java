package com.effectivesoft.usermanagement.web.controller;

import com.effectivesoft.usermanagement.service.UserService;
import com.effectivesoft.usermanagement.web.auth.UserLockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class LoginController extends AbstractController {

    private static final String ERROR_LOGIN_MESSAGE_PARAM = "errorLoginMessage";
    private static final String ERROR_LOGIN_ACCOUNT_LOCKED_MESSAGE_CODE = "login.error.account.locked";
    private static final String ERROR_LOGIN_PASSWORD_INCORRECT_MESSAGE_CODE = "login.error.password.incorrect";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @RequestMapping(value = LOGIN_MAPPING, method = RequestMethod.GET)
    public String goToLogin(Model model, HttpServletRequest request, @RequestParam(value = ERROR_LOGIN_PARAM, required = false) String error) {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return REDIRECT + DASHBOARD_MAPPING;
        }
        if (error != null) {
            HttpSession httpSession = request.getSession(false);
            if (httpSession != null) {
                AuthenticationException exception = (AuthenticationException)httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                if (exception != null) {
                    if (exception.getCause() instanceof UserLockedException) {
                        model.addAttribute(ERROR_LOGIN_MESSAGE_PARAM, messageSource.getMessage(ERROR_LOGIN_ACCOUNT_LOCKED_MESSAGE_CODE, null, DEFAULT_LOCALE));
                    } else {
                        model.addAttribute(ERROR_LOGIN_MESSAGE_PARAM, messageSource.getMessage(ERROR_LOGIN_PASSWORD_INCORRECT_MESSAGE_CODE, null, DEFAULT_LOCALE));
                    }
                }
            }
        }

        return LOGIN_VIEW;
    }


}
