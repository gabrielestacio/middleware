package com.middleware.annotations;

import com.middleware.lifecycle_management.Strategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lifecycle {
	
    public Strategy strategy() default Strategy.STATIC_INSTANCE;

}
