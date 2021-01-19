package com.effectivesoft.usermanagement.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum PermissionObjectType {
    MANAGEDSERVER("MANAGEDSERVER", "Managed Server", 1),
    JMSSERVER("JMSSERVER", "JMS Servers", 2),
    JMSMODULE("JMSMODULE", "JMS Modules", 3),
    DATASOURCE("DATASOURCE", "Data Sources", 4),
    MACHINE("MACHINE", "Machines", 5),
    CLUSTER("CLUSTER", "Clusters", 6),
    LOG("LOG", "Logs", 7),
    REPORT("REPORT", "Reports", 8);

    private String name;
    private String displayName;
    private Integer sequenceNumber;

    PermissionObjectType(String name, String displayName, Integer sequenceNumber) {
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

    public static PermissionObjectType getByName(String name){
        if(name != null){
            for (PermissionObjectType type : PermissionObjectType.values()){
                String currentValueName = type.getName();
                if(currentValueName.equals(name)){
                    return type;
                }
            }
        }
        return null;
    }

    public static List<PermissionObjectType> toArrayList(){
        List<PermissionObjectType> types = Arrays.asList(PermissionObjectType.values());
        Collections.sort(types, new PermissionObjectTypeComparator());
        return types;
    }

    private static class PermissionObjectTypeComparator implements Comparator<PermissionObjectType>{
        @Override
        public int compare(PermissionObjectType first, PermissionObjectType second) {
            return first.getSequenceNumber().compareTo(second.getSequenceNumber());
        }
    }
}
