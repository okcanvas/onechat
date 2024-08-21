package io.bizbee.onechat.server.exception;

import io.bizbee.onechat.common.core.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
public class BusinessException extends RuntimeException implements Serializable {

  protected Integer code;
  protected String message;

  public BusinessException() {
    this(ResultCode.COMMON_ERROR);
  }

  public BusinessException(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public BusinessException(ResultCode resultCode, String message) {
    this.code = resultCode.getCode();
    this.message = message;
  }

  public BusinessException(ResultCode resultCode) {
    this.code = resultCode.getCode();
    this.message = resultCode.getMsg();
  }

  public BusinessException(String message) {
    this.code = ResultCode.PROGRAM_ERROR.getCode();
    this.message = message;
  }

}
