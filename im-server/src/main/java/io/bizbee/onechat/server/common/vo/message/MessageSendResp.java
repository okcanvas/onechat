package io.bizbee.onechat.server.common.vo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageSendResp implements Serializable {

  private Long id;

  private String content;

}
