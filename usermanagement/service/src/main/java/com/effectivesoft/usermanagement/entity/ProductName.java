package com.effectivesoft.usermanagement.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum ProductName {
    ORACLE_WEBLOGIC_SERVER_12_C("Oracle WebLogic Server 12c", 1),
    ORACLE_WEBLOGIC_SERVER_11_G("Oracle WebLogic Server 11g", 2);

    private String name;
    private Integer sequenceNumber;

    ProductName(String name, Integer sequenceNumber) {
        this.name = name;
        this.sequenceNumber = sequenceNumber;
    }

    public String getName() {
        return name;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public static List<ProductName> toArrayList(){
        List<ProductName> productNames = Arrays.asList(ProductName.values());
        Collections.sort(productNames, new ProductNameComparator());
        return productNames;
    }

    public static ProductName getByName(String name){
        if(name != null){
            for (ProductName productName : ProductName.values()){
                String currentValueName = productName.getName();
                if(currentValueName.equals(name)){
                    return productName;
                }
            }
        }
        return null;
    }

    private static class ProductNameComparator implements Comparator<ProductName>{
        @Override
        public int compare(ProductName first, ProductName second) {
            return first.getSequenceNumber().compareTo(second.getSequenceNumber());
        }
    }
}
