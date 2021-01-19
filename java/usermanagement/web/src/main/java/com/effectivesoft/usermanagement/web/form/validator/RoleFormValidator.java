package com.effectivesoft.usermanagement.web.form.validator;

import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.service.RoleService;
import com.effectivesoft.usermanagement.web.form.RoleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Component
public class RoleFormValidator {

    private static final String ROLE_FORM_ATTRIBUTE = "roleForm";
    private static final String NAME_REQURED_MESSAGE_CODE = "role.name.required";
    private static final String NAME_EXISTS_MESSAGE_CODE = "role.name.exists";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RoleService roleService;

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public List<ObjectError> validate(RoleForm form) {
        List<ObjectError> errors = new ArrayList<ObjectError>();

        ObjectError error;
        if (form.getName() == null || form.getName().isEmpty()){
            error = new ObjectError(ROLE_FORM_ATTRIBUTE, messageSource.getMessage(NAME_REQURED_MESSAGE_CODE, null, DEFAULT_LOCALE));
            errors.add(error);
        } else {
            Role role = roleService.findByName(form.getName());
            if (role != null && role.getId() != form.getId()){
                error = new ObjectError(ROLE_FORM_ATTRIBUTE, messageSource.getMessage(NAME_EXISTS_MESSAGE_CODE, null, DEFAULT_LOCALE));
                errors.add(error);
            }
        }

        return errors;
    }
}
