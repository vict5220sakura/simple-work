package com.vict.framework.web;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE,
})
public @interface ApiPrePath {
}
