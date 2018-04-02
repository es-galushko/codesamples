package com.effectivesoft.usermanagement.web.form.validator;

import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.service.UserService;
import com.effectivesoft.usermanagement.web.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class UserFormValidator {

    private static final String USER_FORM_ATTRIBUTE = "userForm";
    private static final String USERNAME_REQURED_MESSAGE_CODE = "user.username.required";
    private static final String PASSWORD_REQURED_MESSAGE_CODE = "user.password.required";
    private static final String EMAIL_REQURED_MESSAGE_CODE = "user.email.required";
    private static final String USERNAME_EXISTS_MESSAGE_CODE = "user.username.exists";
    private static final String EMAIL_EXISTS_MESSAGE_CODE = "user.email.exists";
    private static final String EMAIL_INVALID_MESSAGE_CODE = "user.email.invalid";

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    public List<ObjectError> validate(UserForm form) {
        List<ObjectError> errors = new ArrayList<ObjectError>();

        String username = form.getUsername();
        String password = form.getPassword();
        String email = form.getEmail();

        ObjectError error;

        if (username == null || username.isEmpty()) {
            error = new ObjectError(USER_FORM_ATTRIBUTE, messageSource.getMessage(USERNAME_REQURED_MESSAGE_CODE, null, DEFAULT_LOCALE));
            errors.add(error);
        } else {
            User user = userService.findByUsername(username);
            if (user != null && user.getId() != form.getId()) {
                error = new ObjectError(USER_FORM_ATTRIBUTE, messageSource.getMessage(USERNAME_EXISTS_MESSAGE_CODE, null, DEFAULT_LOCALE));
                errors.add(error);
            }
        }

        if (password == null || password.isEmpty()) {
            error = new ObjectError(USER_FORM_ATTRIBUTE, messageSource.getMessage(PASSWORD_REQURED_MESSAGE_CODE, null, DEFAULT_LOCALE));
            errors.add(error);
        }

        if (email == null || email.isEmpty()) {
            error = new ObjectError(USER_FORM_ATTRIBUTE, messageSource.getMessage(EMAIL_REQURED_MESSAGE_CODE, null, DEFAULT_LOCALE));
            errors.add(error);
        } else {

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(form.getEmail());

            if (!matcher.matches()) {
                error = new ObjectError(USER_FORM_ATTRIBUTE, messageSource.getMessage(EMAIL_INVALID_MESSAGE_CODE, null, DEFAULT_LOCALE));
                errors.add(error);
            }

            User user = userService.findByEmail(email);
            if (user != null && user.getId() != form.getId()) {
                error = new ObjectError(USER_FORM_ATTRIBUTE, messageSource.getMessage(EMAIL_EXISTS_MESSAGE_CODE, null, DEFAULT_LOCALE));
                errors.add(error);
            }

        }

        return errors;
    }
}
