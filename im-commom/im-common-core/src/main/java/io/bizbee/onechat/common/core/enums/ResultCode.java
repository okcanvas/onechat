package io.bizbee.onechat.common.core.enums;

public enum ResultCode {

  SUCCESS(200, "success"), NO_LOGIN(400, "not login"), INVALID_TOKEN(401, "token error"),
  PROGRAM_ERROR(500, "system is busy, please try again later."), PASSWOR_ERROR(10001, "password is incorrect"),
  USERNAME_ALREADY_REGISTER(10003, "This name has been registered"), NO_AVAILABLE_SERVICES(10004, "services available"),

  COMMON_ERROR(501, "error"), ANONYMOUSE_USER_NO_ACTION(502, "anonymous users are prohibited");

  private int code;
  private String msg;

  ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
