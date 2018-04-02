package com.effectivesoft.payment.service;

import com.stripe.model.Charge;

public interface StripeService {

    Charge createCharge(String customerId, String cardId, Integer amount, String description);
}
