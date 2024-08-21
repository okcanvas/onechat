package io.bizbee.onechat.common.core.enums;

public enum IMListenerType {

  ALL(0, "all"), PRIVATE_MESSAGE(1, "private message"), GROUP_MESSAGE(2, "group message");

  private Integer code;

  private String desc;

  IMListenerType(Integer index, String desc) {
    this.code = index;
    this.desc = desc;
  }

  public String description() {
    return desc;
  }

  public Integer code() {
    return this.code;
  }

}
