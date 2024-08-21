package io.bizbee.onechat.common.core.enums;

import lombok.Getter;

@Getter
public enum CommonEnums {


  Yes(1,"yes"),
  No(0,"no")
    ;


  Integer code;

  String desc;

  CommonEnums(Integer code,String desc){
    this.code = code;
    this.desc = desc;
  }

}
