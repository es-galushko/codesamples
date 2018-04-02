package com.effectivesoft.usermanagement.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum Operator {
    LT("<", 1),
    GT(">", 2),
    EQ("=", 3),
    LE("<=", 4),
    GE(">=", 5),
    NE("!=", 6);

    private String symbol;
    private Integer sequenceNumber;

    Operator(String symbol, Integer sequenceNumber) {
        this.symbol = symbol;
        this.sequenceNumber = sequenceNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public static List<Operator> toArrayList(){
        List<Operator> operators = Arrays.asList(Operator.values());
        Collections.sort(operators, new OperatorComparator());
        return operators;
    }

    private static class OperatorComparator implements Comparator<Operator>{
        @Override
        public int compare(Operator o1, Operator o2) {
            return o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
        }
    }
}
