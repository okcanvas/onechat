package io.bizbee.onechat.server;

import io.bizbee.onechat.server.util.IP;
import io.bizbee.onechat.server.util.IpUtil;
import org.junit.jupiter.api.Test;
import org.lionsoul.ip2region.xdb.Searcher;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class IpUtilTest {

  @Test
  public void t1() {
    URL resource = IpUtilTest.class.getClassLoader().getResource("ip/ip2region.xdb");
    String dbPath = resource.getPath();

    byte[] cBuff;
    try {
      cBuff = Searcher.loadContentFromFile(dbPath);
    } catch (Exception e) {
      System.out.printf("failed to load content from `%s`: %s\n", dbPath, e);
      return;
    }

    Searcher searcher;
    try {
      searcher = Searcher.newWithBuffer(cBuff);
    } catch (Exception e) {
      System.out.printf("failed to create content cached searcher: %s\n", e);
      return;
    }

    String ip = null;
    try {
      ip = "127.0.0.1";
      long sTime = System.nanoTime();
      String region = searcher.search(ip);
      String strs = searcher.search(ip);
      String[] strings = strs.split("\\|");
      String city = strings[3];
      long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
      System.out
          .printf("{region: %s,,city=%s, ioCount: %d, took: %d μs}\n", region, city,
              searcher.getIOCount(), cost);
    } catch (Exception e) {
      System.out.printf("failed to search(%s): %s\n", ip, e);
    }

  }


  @Test
  public void t2() {
    System.out.println(IpUtil.findGeography("127.0.0.1"));
    System.out.println(IpUtil.search("127.0.0.1"));
  }

}
