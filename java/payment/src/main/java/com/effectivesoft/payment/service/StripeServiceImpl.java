package com.effectivesoft.payment.service;

import com.effectivesoft.payment.exception.BadDataException;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class StripeServiceImpl implements StripeService {

    private static final Logger logger = LoggerFactory.getLogger(StripeServiceImpl.class);

    public static final String CURRENCY = "usd";

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    void initialize() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Charge createCharge(String customerId, String cardId, Integer amount, String description) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", CURRENCY);
        chargeParams.put("description", description);
        chargeParams.put("customer", customerId);
        chargeParams.put("source", cardId);
        try {
            Charge charge = Charge.create(chargeParams);
            logger.debug("charge: " + charge);
            return charge;
        } catch (AuthenticationException | InvalidRequestException | CardException | APIConnectionException
                | APIException e) {
            logger.error(e.getMessage(), e);
            throw new BadDataException("", "error.stripe.message", e.getMessage());
        }
    }

}
