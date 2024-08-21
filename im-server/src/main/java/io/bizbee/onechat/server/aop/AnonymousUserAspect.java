package io.bizbee.onechat.server.aop;

import io.bizbee.onechat.common.core.enums.ResultCode;
import io.bizbee.onechat.server.aop.annotation.AnonymousUserCheck;
import io.bizbee.onechat.server.common.enums.UserEnum;
import io.bizbee.onechat.server.exception.BusinessException;
import io.bizbee.onechat.server.util.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Slf4j
@Aspect
@Component
@Order(0)
public class AnonymousUserAspect {

  @Before("@within(io.bizbee.onechat.server.aop.annotation.AnonymousUserCheck) "
      + "|| @annotation(io.bizbee.onechat.server.aop.annotation.AnonymousUserCheck)")
  public void interceptor(JoinPoint point) throws NoSuchMethodException {
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    Method targetMethod = point.getTarget()
        .getClass()
        .getDeclaredMethod(methodSignature.getName(),
            methodSignature.getMethod().getParameterTypes());
    AnonymousUserCheck anonymousUserCheck = Optional
        .ofNullable(targetMethod.getAnnotation(AnonymousUserCheck.class))
        .orElse(point.getTarget().getClass().getAnnotation(AnonymousUserCheck.class));
    if (anonymousUserCheck == null) {
      return;
    }
    SessionContext.UserSessionInfo session = SessionContext.getSession();
    if (session == null) {
      throw new BusinessException(ResultCode.INVALID_TOKEN);
    }
    if (UserEnum.AccountType.Anonymous.getCode().equals(session.getAccountType())) {
      throw new BusinessException(ResultCode.ANONYMOUSE_USER_NO_ACTION);
    }
  }

}
