package io.bizbee.onechat.common.core.enums;

public enum MessageType {

  TEXT(0, "text"), FILE(1, "file"), IMAGE(2, "image"), VIDEO(3, "video"), TIP(10, "tip"),

  RTC_CALL(101, "call"), RTC_ACCEPT(102, "accept"), RTC_REJECT(103, "reject"), RTC_CANCEL(104, "cancel"),
  RTC_FAILED(105, "failed"), RTC_HANDUP(106, "handup"), RTC_CANDIDATE(107, "candidate");

  private Integer code;

  private String desc;

  MessageType(Integer index, String desc) {
    this.code = index;
    this.desc = desc;
  }

  public static MessageType findByCode(Integer type) {
    for (MessageType m : values()) {
      if (m.code.equals(type)) {
        return m;
      }
    }
    return null;
  }

  public String description() {
    return desc;
  }

  public Integer code() {
    return this.code;
  }
}
