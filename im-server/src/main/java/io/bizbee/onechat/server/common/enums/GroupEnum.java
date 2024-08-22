package io.bizbee.onechat.server.common.enums;

import lombok.Getter;

public interface GroupEnum {

  @Getter
  enum GroupType {

    Plain(0, "정상"), Anonymous(1, "익명"),
    ;

    private Integer code;

    private String msg;

    GroupType(Integer code, String msg) {
      this.code = code;
      this.msg = msg;
    }

  }

}
