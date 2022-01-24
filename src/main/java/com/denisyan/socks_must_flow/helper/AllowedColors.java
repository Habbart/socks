package com.denisyan.socks_must_flow.helper;

public enum AllowedColors {
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    BLACK("black"),
    WHITE("white"),
    YELLOW("yellow"),
    ORANGE("orange"),
    VIOLET("violet"),
    INDIGO("indigo"),
    PINK("pink");

    private final String fieldName;

    AllowedColors(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }


}
