package io.bizbee.onechat.server;

import cn.hutool.core.bean.BeanUtil;
import io.bizbee.onechat.common.core.model.GroupMessageInfo;
import io.bizbee.onechat.server.common.entity.GroupMessage;

public class BeanCopyTest {

  public static void main(String[] args) {
    GroupMessage groupMessage = new GroupMessage();
    groupMessage.setGroupId(1L);
    groupMessage.setContent("aaa");

    GroupMessageInfo groupMessageInfo = BeanUtil.copyProperties(groupMessage,
        GroupMessageInfo.class);
    System.out.println(groupMessageInfo.getGroupId());
    System.out.println(groupMessageInfo.getContent());
  }
}
