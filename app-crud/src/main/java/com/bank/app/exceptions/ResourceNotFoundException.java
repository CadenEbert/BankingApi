package com.bank.app.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName){
        super("Resource " + resourceName + " not found with name " + fieldName + " and field " + fieldName);
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field,  Long fieldId){
        super("Resource " + resourceName + " not found with name " + field + " and id " + fieldId);
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;

    }

}
