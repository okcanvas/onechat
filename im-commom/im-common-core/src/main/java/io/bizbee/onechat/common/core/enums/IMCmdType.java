package io.bizbee.onechat.common.core.enums;

public enum IMCmdType {

  ERROR(-1, "error"), LOGIN(0, "login"), HEART_BEAT(1, "heart beat"), FORCE_LOGUT(2, "forced offline"),
  PRIVATE_MESSAGE(3, "private message"), GROUP_MESSAGE(4, "group message");

  private Integer code;

  private String desc;

  IMCmdType(Integer index, String desc) {
    this.code = index;
    this.desc = desc;
  }

  public static IMCmdType fromCode(Integer code) {
    for (IMCmdType typeEnum : values()) {
      if (typeEnum.code.equals(code)) {
        return typeEnum;
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
