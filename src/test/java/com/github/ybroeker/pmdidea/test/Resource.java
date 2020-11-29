package com.github.ybroeker.pmdidea.test;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Resource {

    String value();

}
