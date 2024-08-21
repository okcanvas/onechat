package io.bizbee.onechat.connector.remote.netty;

import cn.hutool.core.date.DateUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import io.bizbee.onechat.common.core.utils.SpringContextHolder;
import io.bizbee.onechat.connector.contant.ConnectorConst;
import io.bizbee.onechat.connector.listener.event.UserEvent;
import io.bizbee.onechat.connector.processor.MessageProcessor;
import io.bizbee.onechat.connector.processor.ProcessorFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IMChannelHandler extends SimpleChannelInboundHandler<IMSendInfo> {

  /**
   * @param ctx
   * @param sendInfo
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, IMSendInfo sendInfo) throws Exception {
    if (sendInfo == null) {
      return;
    }
    IMCmdType imCmdType = IMCmdType.fromCode(sendInfo.getCmd());
    if (imCmdType == null) {
      return;
    }
    
    MessageProcessor processor = ProcessorFactory.createProcessor(imCmdType);
    if (processor == null) {
      return;
    }
    processor.process(ctx, sendInfo.getData());
  }

  /**
   * @param ctx
   * @param cause
   * @throws Exception
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error(cause.getMessage());
    Long userId = this.getUserIdFromChannelContext(ctx);
    SpringContextHolder.sendEvent(UserEvent.buildOfflineEvent(userId, ctx));
    ctx.close();
  }

  /**
   * @param ctx
   * @throws Exception
   */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    log.info(ctx.channel().id().asLongText());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Long userId = this.getUserIdFromChannelContext(ctx);
    ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId);
    
    if (context != null && ctx.channel().id().equals(context.channel().id())) {
      SpringContextHolder.sendEvent(UserEvent.buildOfflineEvent(userId, ctx));
      // channel
      UserChannelCtxMap.removeChannelCtx(userId);
      log.info("userId:{}", userId);
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent) evt).state();
      log.info("IdleStateEvent,time={},state={}", DateUtil.now(), state);
      if (state == IdleState.ALL_IDLE) {
        // 
        Long userId = this.getUserIdFromChannelContext(ctx);
        if (userId == null) {
          return;
        }
        SpringContextHolder.sendEvent(UserEvent.buildOfflineEvent(userId, ctx));
        log.info("id:{} ", userId);
        ctx.channel().close();
      }
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  private Long getUserIdFromChannelContext(ChannelHandlerContext ctx) {
    AttributeKey<Long> attr = AttributeKey.valueOf(ConnectorConst.USER_ID);
    Long userId = ctx.channel().attr(attr).get();
    return userId;
  }
}
