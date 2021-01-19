package com.effectivesoft.usermanagement.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public enum AlertObjectType {
    MANAGEDSERVER("MANAGEDSERVER", "Managed Server", 1),
    DATASOURCE("DATASOURCE", "Data Sources", 2),
    JMSSERVER("JMSSERVER", "JMS Servers", 3),
    JMSMODULE("JMSMODULE", "JMS Modules", 4),
    JMSRESOURCE("JMSRESOURCE", "JMS Resources", 5),
    MACHINE("MACHINE", "Machines", 6),
    CLUSTER("CLUSTER", "Clusters", 7);

    private String name;
    private String displayName;
    private Integer sequenceNumber;

    AlertObjectType(String name, String displayName, Integer sequenceNumber) {
        this.name = name;
        this.displayName = displayName;
        this.sequenceNumber = sequenceNumber;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public static List<AlertObjectType> toArrayList(){
        List<AlertObjectType> types = Arrays.asList(AlertObjectType.values());
        Collections.sort(types, new AlertObjectTypeComparator());
        return types;
    }

    private static class AlertObjectTypeComparator implements Comparator<AlertObjectType>{
        @Override
        public int compare(AlertObjectType first, AlertObjectType second) {
            return first.getSequenceNumber().compareTo(second.getSequenceNumber());
        }
    }
}
