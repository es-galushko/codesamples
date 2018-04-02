package com.effectivesoft.usermanagement.web.form.validator;

import com.effectivesoft.usermanagement.entity.Alert;
import com.effectivesoft.usermanagement.service.AlertServise;
import com.effectivesoft.usermanagement.web.form.AlertForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class AlertFormValidator {

    private static final String ALERT_FORM_ATTRIBUTE = "alertForm";
    private static final String NAME_REQURED_MESSAGE_CODE = "alert.name.required";

    @Autowired
    private MessageSource messageSource;

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public List<ObjectError> validate(AlertForm form) {
        List<ObjectError> errors = new ArrayList<ObjectError>();

        ObjectError error;

        if (form.getName() == null || form.getName().isEmpty()){
            error = new ObjectError(ALERT_FORM_ATTRIBUTE, messageSource.getMessage(NAME_REQURED_MESSAGE_CODE, null, DEFAULT_LOCALE));
            errors.add(error);
        }

        return errors;
    }
}
