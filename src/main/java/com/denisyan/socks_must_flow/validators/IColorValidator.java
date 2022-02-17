package com.denisyan.socks_must_flow.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ColorValidator.class)
public @interface IColorValidator {


    String message() default "prohibited color, please check allowed color";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
