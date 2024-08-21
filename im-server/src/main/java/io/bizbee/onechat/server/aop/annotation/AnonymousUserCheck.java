package io.bizbee.onechat.server.aop.annotation;

import java.lang.annotation.*;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousUserCheck {

}
