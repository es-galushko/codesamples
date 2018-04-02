package com.effectivesoft.usermanagement.web.dto;

import org.springframework.data.domain.Sort;

import java.io.Serializable;


public class SortOrder implements Serializable {
    private Sort.Direction direction;
    private String property;

    public SortOrder() {
    }

    public SortOrder(Sort.Direction direction, String property) {
        this.direction = direction;
        this.property = property;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return property + "," + direction.toString().toLowerCase();
    }
}
