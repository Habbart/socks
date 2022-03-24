package com.denisyan.socks_must_flow.validators;

/**
 * Operation for filtering cotton percentage in socks
 */

public enum AllowedOperation {
    MORE_THAN("morethan"),
    LESS_THAN("lessthan"),
    EQUAL("equal");

    private final String fieldName;

    AllowedOperation(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

}
