package com.effectivesoft.payment.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "ChargeRequest")
public class ChargeRequest {

    @ApiModelProperty(value = "Customer ID", required = true)
    @NotBlank
    private String customerId;

    @ApiModelProperty(value = "Card ID", required = true)
    @NotBlank
    private String cardId;

    @ApiModelProperty(value = "Description of the charge", required = true)
    @NotBlank
    private String description;

    @ApiModelProperty(value = "Amount in cents", required = true)
    @Min(50)
    @NotNull
    private Integer amount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{customerId='" + customerId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
