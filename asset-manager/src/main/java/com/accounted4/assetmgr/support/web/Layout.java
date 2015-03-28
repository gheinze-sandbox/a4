package com.accounted4.assetmgr.support.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation to flag the view template (layout) a controller should use.
 * See: http://blog.codeleak.pl/2013/11/thymeleaf-template-layouts-in-spring.html
 * 
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Layout {
    String value();
}
