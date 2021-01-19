package com.effectivesoft.usermanagement.web.auth;

import com.effectivesoft.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final String USERNAME = "username";

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            String requestedUsername = request.getParameter(USERNAME);
            userService.failureLogin(requestedUsername);
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
