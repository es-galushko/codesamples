package com.effectivesoft.payment.web.vo;

import com.stripe.model.Charge;

public class ChargeResponse {

    private String chargeId;
    private Long createdTimeSec;

    public ChargeResponse() {
    }

    public ChargeResponse(String chargeId, Long createdTimeSec) {
        this.chargeId = chargeId;
        this.createdTimeSec = createdTimeSec;
    }

    public ChargeResponse(Charge charge) {
        this(charge.getId(), charge.getCreated());
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public Long getCreatedTimeSec() {
        return createdTimeSec;
    }

    public void setCreatedTimeSec(Long createdTimeSec) {
        this.createdTimeSec = createdTimeSec;
    }
}
