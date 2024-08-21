package io.bizbee.onechat.common.log;

import io.bizbee.onechat.common.log.aop.ApiLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogAutoConfig {

  @Bean
  public ApiLogAspect apiLogAspect() {
    return new ApiLogAspect();
  }
}
