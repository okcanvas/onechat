package io.bizbee.onechat.connector.remote.netty.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.bizbee.onechat.common.core.contant.AppConst;
import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.common.core.utils.SpringContextHolder;
import io.bizbee.onechat.connector.config.AppConfigProperties;
import io.bizbee.onechat.connector.listener.event.NodeRegisterEvent;
import io.bizbee.onechat.connector.remote.netty.AbstractRemoteServer;
import io.bizbee.onechat.connector.remote.netty.IMChannelHandler;
import io.bizbee.onechat.connector.remote.netty.factory.NettyEventLoopFactory;
import io.bizbee.onechat.connector.remote.netty.ws.endecode.MessageProtocolDecoder;
import io.bizbee.onechat.connector.remote.netty.ws.endecode.MessageProtocolEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebSocketServer extends AbstractRemoteServer {

  public WebSocketServer() {
    super();
  }

  @Override
  protected AppConfigProperties.TcpNode nodeInfo() {
    return appConfigProperties.getWs();
  }

  @Override
  public void start() {
    AppConfigProperties.TcpNode nodeInfo = nodeInfo();
    bootstrap.group(bossGroup, workGroup)
        .channel(NettyEventLoopFactory.serverSocketChannelClass())
        .option(ChannelOption.SO_BACKLOG, 1024)
        .option(ChannelOption.SO_REUSEADDR, true)
        .childOption(ChannelOption.SO_KEEPALIVE, false)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childHandler(new ChannelInitializer<Channel>() {

          @Override
          protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("http-codec", new HttpServerCodec());
            pipeline.addLast("aggregator", new HttpObjectAggregator(65535));
            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
            pipeline.addLast("encode", new MessageProtocolEncoder());
            pipeline.addLast("decode", new MessageProtocolDecoder());
            addPipeline(pipeline);
          }
        });

    try {
      Integer port = super.port();
      Channel channel = bootstrap.bind(port).sync().channel();
      this.ready = true;
      SpringContextHolder.sendEvent(NodeRegisterEvent.builder()
          .netProtocolEnum(NetProtocolEnum.WS)
          .port(port)
          .registerTime(System.currentTimeMillis())
          .build());
      log.info("websocket server {}", port);
    } catch (InterruptedException e) {
      log.error("websocket server ", e);
    }
  }

}
