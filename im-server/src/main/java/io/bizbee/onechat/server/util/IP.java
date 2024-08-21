package io.bizbee.onechat.server.util;

import lombok.Data;

@Data
public class IP {

  private String country;
  private String province;
  private String city;
  private String isp;
  private String region;

  public static IP Local() {
    IP ip = new IP();
    ip.setCountry("country");
    ip.setProvince("province");
    ip.setCity("city");
    ip.setRegion("region");
    ip.setRegion("0");
    return ip;
  }
}