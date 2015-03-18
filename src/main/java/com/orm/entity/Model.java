package com.orm.entity;

import java.util.List;

public class Model {

    private List<String> propertyNameList;
    private List<String> propertyTypeList;
    private String pkType;
    
    public Model(List<String> propertyNameList, List<String> propertyTypeList, String pkType) {
        this.propertyNameList = propertyNameList;
        this.propertyTypeList = propertyTypeList;
        this.pkType = pkType;
    }

    public String getPkType() {
        return pkType;
    }

    public void setPkType(String pkType) {
        this.pkType = pkType;
    }

    public List<String> getPropertyNameList() {
        return propertyNameList;
    }

    public void setPropertyNameList(List<String> propertyNameList) {
        this.propertyNameList = propertyNameList;
    }

    public List<String> getPropertyTypeList() {
        return propertyTypeList;
    }

    public void setPropertyTypeList(List<String> propertyTypeList) {
        this.propertyTypeList = propertyTypeList;
    }
}
