package io.bizbee.onechat.connector.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class AbstractPullMessageTask {

  private ExecutorService executorService = Executors.newFixedThreadPool(1);

  protected final static Integer ONES_PULL_MESSAGE_COUNT = 200;

  @PostConstruct
  public void init() {
    executorService.execute(new Runnable() {

      @SneakyThrows
      @Override
      public void run() {
        try {
          pullMessage();
          Thread.sleep(10);
        } catch (Exception e) {
          log.error("task scheduling exception", e);
          Thread.sleep(200);
        }
        if (!executorService.isShutdown()) {
          executorService.execute(this);
        }
      }
    });
  }

  @PreDestroy
  public void destroy() {
    log.info("{} task closed", this.getClass().getSimpleName());
    executorService.shutdown();
  }

  public abstract void pullMessage();
}
