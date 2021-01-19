package com.effectivesoft.payment.web;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Value("${stripe.webhook.endpoint.secret}")
    private String endpointSecret;

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public void webhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String body,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            Event event = Webhook.constructEvent(body, signature, endpointSecret);
            logger.debug("Stripe webhook event: " + event);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
