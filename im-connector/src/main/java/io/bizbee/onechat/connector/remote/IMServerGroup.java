package io.bizbee.onechat.connector.remote;

import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.core.contant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class IMServerGroup implements CommandLineRunner {

  public static volatile long serverId = -1;

  @Autowired
  private AppCache appCache;

  @Autowired
  private List<IMServer> imServers;

  @Override
  public void run(String... args) throws Exception {
    // SERVER_ID
    serverId = appCache.incr(RedisKey.IM_MAX_SERVER_ID);
    Iterator<IMServer> iterator = imServers.iterator();
    while (iterator.hasNext()) {
      IMServer imServer = iterator.next();
      if (imServer.enable()) {
        imServer.start();
      }
    }
  }

  @PreDestroy
  public void destroy() {
    for (IMServer imServer : imServers) {
      imServer.stop();
    }
  }
}
