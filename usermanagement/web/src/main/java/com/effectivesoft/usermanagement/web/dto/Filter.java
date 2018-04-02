package com.effectivesoft.usermanagement.web.dto;

import java.io.Serializable;

public class Filter implements Serializable {

    /**
     * request property attribute
     */
    private String propertyAttribute;
    private String value;

    public Filter() {
    }

    public Filter(String propertyAttribute, String value) {
        this.propertyAttribute = propertyAttribute;
        this.value = value;
    }

    public String getPropertyAttribute() {
        return propertyAttribute;
    }

    public void setPropertyAttribute(String propertyAttribute) {
        this.propertyAttribute = propertyAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
