package io.bizbee.onechat.server.common.entity;

import lombok.Data;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
public class ChatSession {

  private Long id;

  /**
   * 对方id
   */
  private Long targetId;

  /**
   * 聊天类型
   */
  private String chatType;

  /**
   * 归属用户id
   */
  private Long ownId;

  private Long updateTime;

  /**
   * 是否置顶
   */
  private Long topFlag;
}
