package io.bizbee.onechat.common.core.enums;

public enum IMSendCode {

  SUCCESS(0, "success"), NOT_ONLINE(1, "online"), NOT_FIND_CHANNEL(2, "not find channel"),
  UNKONW_ERROR(9999, "error");

  private int code;
  private String desc;

  IMSendCode(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public String description() {
    return desc;
  }

  public Integer code() {
    return this.code;
  }

}
