package com.jun.tools.annotaitions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD ,ElementType.TYPE })
public @interface Ignore {
	/**
	 * The optional reason why the test is ignored.
	 */
	String value() default ""; 
}
