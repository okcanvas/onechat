package io.bizbee.onechat.common.core.enums;

public enum FileType {

  FILE(0, "file"), IMAGE(1, "image"), VIDEO(2, "video"), AUDIO(3, "audio");

  private Integer code;

  private String desc;

  FileType(Integer index, String desc) {
    this.code = index;
    this.desc = desc;
  }

  public static FileType fromCode(Integer code) {
    for (FileType typeEnum : values()) {
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
