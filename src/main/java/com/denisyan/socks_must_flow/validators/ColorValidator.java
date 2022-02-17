package com.denisyan.socks_must_flow.validators;


import com.denisyan.socks_must_flow.helper.AllowedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;

/**
 * annotation to validate Color parametr
 * Depends from Allowed Color Enum
 */

public class ColorValidator implements ConstraintValidator<IColorValidator, String> {

    private final Logger logger = LoggerFactory.getLogger("IColorValidator logger");


    @Override
    public boolean isValid(String color, ConstraintValidatorContext constraintValidatorContext) {
        logger.debug("валидируем через аннотацию");
        if(color == null) return false;
        return Arrays.stream(AllowedColors.values()).map(AllowedColors::getFieldName).anyMatch(s -> s.toLowerCase(Locale.ROOT).equals(color.toLowerCase(Locale.ROOT)));
    }

    @Override
    public void initialize(IColorValidator constraintAnnotation) {
        //no need to initialize
    }


}
