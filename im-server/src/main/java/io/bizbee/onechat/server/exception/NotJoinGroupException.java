package io.bizbee.onechat.server.exception;

import io.bizbee.onechat.common.core.enums.ResultCode;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
public class NotJoinGroupException extends BusinessException {

  public NotJoinGroupException(ResultCode resultCode, String message) {
    code = resultCode.getCode();
    message = message;
  }

}
