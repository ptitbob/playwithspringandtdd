package org.shipstone.sandbox.configuration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author François Robert
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
@Documented
public @interface ShipstoneLogger {

  String name() default "";

}
