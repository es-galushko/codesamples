package com.effectivesoft.payment.web;

import com.effectivesoft.payment.web.vo.ChargeRequest;
import com.effectivesoft.payment.web.vo.ChargeResponse;
import com.effectivesoft.payment.service.StripeService;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PaymentController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private StripeService stripeService;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public ChargeResponse createCharge(
            @Valid @RequestBody ChargeRequest request,
            BindingResult bindingResult) {
        logger.debug("createCharge: " + request);
        handleBindingResult(bindingResult);
        Charge charge = stripeService.createCharge(
                request.getCustomerId(), request.getCardId(), request.getAmount(), request.getDescription());
        return new ChargeResponse(charge);
    }
}
