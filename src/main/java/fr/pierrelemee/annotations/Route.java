package fr.pierrelemee.annotations;

import fr.pierrelemee.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //on class level
public @interface Route {

    String name();

    String path() default "/";

    HttpMethod method() default HttpMethod.GET;
}
