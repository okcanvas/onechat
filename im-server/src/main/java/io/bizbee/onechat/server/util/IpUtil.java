package io.bizbee.onechat.server.util;

import cn.hutool.core.util.StrUtil;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class IpUtil {

  private static final String LOCAL_IP = "127.0.0.1";

  private static String IP_DATA_PATH = "/ip/ip2region.xdb";
  private static IpUtil ipUtil = new IpUtil();
  private static Searcher _searcher = null;

  static {
    InputStream inputStream = null;
    try {
      ClassPathResource classPathResource = new ClassPathResource(IP_DATA_PATH);
      inputStream = classPathResource.getInputStream();
      if (inputStream == null) {
        throw new RuntimeException("Failed to initialize ip message, file does not exist");
      }
      byte[] cBuff;
      cBuff = FileUtil.readToByte(inputStream);
      _searcher = Searcher.newWithBuffer(cBuff);
      log.info("IP information initialization completed---------------------");
    } catch (Exception e) {
      throw new RuntimeException("Initializing IP information failed", e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public static String getIpAddr(HttpServletRequest request) {
    if (request == null) {
      return "unknown";
    }
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Forwarded-For");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
  }


  public static String search(String ip) {
    IP geography = findGeography(ip);
    if (geography == null) {
      return null;
    }
    return geography.getCity();
  }

  public static IP findGeography(String remote) {
    IP ip = null;
    if (StrUtil.isBlank(remote)) {
      return IP.Local();
    }
    try {
      String block = _searcher.search(remote);
      if (block != null) {
        ip = new IP();
        String[] region = block.split("[\\|]");
        if (region.length == 5) {
          ip.setCountry(region[0]);
          if (!StringUtils.isBlank(region[1]) && !region[1].equalsIgnoreCase("null")) {
            ip.setRegion(region[1]);
          } else {
            ip.setRegion("");
          }
          if (!StringUtils.isBlank(region[2]) && !region[2].equalsIgnoreCase("null")) {
            ip.setProvince(region[2]);
          } else {
            ip.setProvince("");
          }
          if (!StringUtils.isBlank(region[3]) && !region[3].equalsIgnoreCase("null")) {
            ip.setCity(region[3]);
          } else {
            ip.setCity("");
          }
          if (!StringUtils.isBlank(region[4]) && !region[4].equalsIgnoreCase("null")) {
            ip.setIsp(region[4]);
          } else {
            ip.setIsp("");
          }
        }
      }
    } catch (Exception ex) {
    }
    return ip;
  }


  /**
   * @return
   */
  public static Integer getIntIp() {
    String ipStr = getIpAddr(SessionContext.getRequest());
    String[] ip = ipStr.split("\\.");
    return (Integer.parseInt(ip[0]) << 24) + (Integer.parseInt(ip[1]) << 16) + (
        Integer.parseInt(ip[2]) << 8)
        + Integer.parseInt(ip[3]);

  }

  public static String getLocalIP() {
    String ip = "";
    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
      InetAddress addr;
      try {
        addr = InetAddress.getLocalHost();
        ip = addr.getHostAddress();
      } catch (UnknownHostException e) {
        log.error("error", e);
      }
      return ip;
    } else {
      try {
        Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
        while (e1.hasMoreElements()) {
          NetworkInterface ni = (NetworkInterface) e1.nextElement();
          if (!ni.getName().equals("eth0")) {
            continue;
          } else {
            Enumeration<?> e2 = ni.getInetAddresses();
            while (e2.hasMoreElements()) {
              InetAddress ia = (InetAddress) e2.nextElement();
              if (ia instanceof Inet6Address) {
                continue;
              }
              ip = ia.getHostAddress();
              return ip;
            }
            break;
          }
        }
      } catch (SocketException e) {
        log.error("error", e);
      }
    }
    return "";
  }

}
