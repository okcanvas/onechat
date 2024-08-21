package io.bizbee.onechat.common.core;

import io.bizbee.onechat.common.core.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreCommonAutoConfiguration {

  @Bean
  public SpringContextHolder springContextHolder() {
    return new SpringContextHolder();
  }

}
