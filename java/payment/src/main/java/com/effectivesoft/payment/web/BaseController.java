package com.effectivesoft.payment.web;

import com.effectivesoft.payment.web.vo.FieldMessage;
import com.effectivesoft.payment.exception.BadDataException;
import com.effectivesoft.payment.web.vo.ErrorResponse;
import com.effectivesoft.payment.exception.BadDataMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class BaseController {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadDataException.class)
    public @ResponseBody
    ErrorResponse handleBadDataException(BadDataException e, WebRequest request,
                                         HttpServletResponse response, Locale locale) {
        List<FieldMessage> errors = e.getMessages().stream().map(
                message -> new FieldMessage(
                        message.getField(),
                        message.getErrorCode(),
                        StringUtils.capitalize(messageSource.getMessage(
                                message.getErrorCode(), message.getArguments(), locale)))
        ).collect(Collectors.toList());
        return new ErrorResponse(errors);
    }

    protected void handleBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<BadDataMessage> badDataMessages = bindingResult.getFieldErrors().stream().map(
                    fieldError -> new BadDataMessage(
                            fieldError.getField(),
                            determineErrorCode(fieldError.getCodes()))
            ).collect(Collectors.toList());
            throw new BadDataException(badDataMessages);
        }
    }

    private String determineErrorCode(String[] codes) {
        if (codes == null || codes.length == 0) {
            return "";
        }
        return codes[0].startsWith("error.") ? codes[codes.length - 1] : codes[0];
    }

}
