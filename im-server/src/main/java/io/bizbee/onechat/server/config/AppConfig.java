package io.bizbee.onechat.server.config;

import io.bizbee.onechat.server.core.algorithm.RouteHandle;
import io.bizbee.onechat.server.core.algorithm.consistenthash.ConsistentHashHandle;
import io.bizbee.onechat.server.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Slf4j
@Configuration
public class AppConfig {

  @Bean
  public RouteHandle routeHandle() {
    return new ConsistentHashHandle();
  }

}
