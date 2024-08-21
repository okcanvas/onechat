package io.bizbee.onechat.connector.remote.netty.tcp.endecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageProtocolDecoder extends ReplayingDecoder {

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
      List<Object> list) throws Exception {
    if (byteBuf.readableBytes() < 4) {
      return;
    }
    long length = byteBuf.readLong();
    ByteBuf contentBuf = byteBuf.readBytes((int) length);
    String content = contentBuf.toString(CharsetUtil.UTF_8);
    ObjectMapper objectMapper = new ObjectMapper();
    IMSendInfo sendInfo = objectMapper.readValue(content, IMSendInfo.class);
    list.add(sendInfo);
  }
}
