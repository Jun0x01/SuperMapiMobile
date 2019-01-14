package com.jun.tools.annotaitions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD ,ElementType.TYPE })
public @interface AnnDisableOther {

	public boolean value() default true;
}
