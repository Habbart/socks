package com.denisyan.socks_must_flow.validators.color_validator;

/**
 * Allowed colors for socks which can be added or requested from warehouse
 * Annotation @IColorValidator depends from this Enum
 */

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
