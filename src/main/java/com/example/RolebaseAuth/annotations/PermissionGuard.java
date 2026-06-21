package com.example.RolebaseAuth.annotations;

import java.lang.annotation.*;

@Documented
//we want document this annotation in javaDoc. Because this is a custom annotation and not from java.
//spring boot annotation are the @.....
@Retention(RetentionPolicy.RUNTIME)
//we want this permission guard security to be actuated during runtime
@Target(ElementType.METHOD)
public @interface PermissionGuard {
    String[] value();

}
