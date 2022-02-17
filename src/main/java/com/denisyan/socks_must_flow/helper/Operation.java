package com.denisyan.socks_must_flow.helper;

/**
 * Operation for filtering cotton percentage in socks
 */

public enum Operation {
    MORE_THAN("morethan"),
    LESS_THAN("lessthan"),
    EQUAL("equal");

    private final String fieldName;

    Operation(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

}
