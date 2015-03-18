package com.orm.entity;

public class Project {
    
    private String basePackageName;
    private String projectName;
  
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getBasePackageName() {
        return basePackageName;
    }
    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }    

}
