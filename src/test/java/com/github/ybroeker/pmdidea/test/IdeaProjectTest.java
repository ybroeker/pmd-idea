package com.github.ybroeker.pmdidea.test;

import java.lang.annotation.*;

import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(IdeaProjectTestExtension.class)
public @interface IdeaProjectTest {
}
