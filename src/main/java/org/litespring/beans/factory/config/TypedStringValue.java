package org.litespring.beans.factory.config;

public class TypedStringValue {
    private final String Value;

    public TypedStringValue(String value) {
        Value = value;
    }
    public String getValue(){
        return  this.Value;
    }
}
