package com.levik.jdbc.configuration.model;

public class ValueType {

    private String type;
    private Object value;

    public ValueType(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
