package com.denisyan.socks_must_flow.validators.color_validator;


import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;

/**
 * annotation to validate Color parametr
 * Depends from Allowed Color Enum
 */

@Slf4j
public class ColorValidator implements ConstraintValidator<IColorValidator, String> {


    @Override
    public boolean isValid(String color, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("валидируем через аннотацию");
        if (color == null) return false;
        return Arrays.stream(AllowedColors.values()).map(AllowedColors::getFieldName).anyMatch(s -> s.toLowerCase(Locale.ROOT).equals(color.toLowerCase(Locale.ROOT)));
    }

    @Override
    public void initialize(IColorValidator constraintAnnotation) {
        //no need to initialize
    }


}
