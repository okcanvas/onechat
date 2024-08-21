package io.bizbee.onechat.server.common.vo.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
public class AnonymousLoginReq implements Serializable {

  @NotNull
  @Size(min = 10, max = 100)
  private String anonymouId;
}
