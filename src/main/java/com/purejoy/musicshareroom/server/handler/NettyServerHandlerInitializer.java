package com.purejoy.musicshareroom.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    private static final Integer READ_TIME_OUT = 3* 60;

    @Resource
    private WebSocketServerHandler webSocketServerHandler;

    @Resource
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
                //连接超时参数
        pipeline.addLast(new ReadTimeoutHandler(READ_TIME_OUT, TimeUnit.SECONDS))
                //http编码
                .addLast("http-codec",new HttpServerCodec())
                //websocket使用
                .addLast(new HttpObjectAggregator(1024 * 64))
                //netty websocket server支持
                .addLast(new WebSocketServerProtocolHandler("/ws",null,true))
                //65 秒没有向客户端发送消息就发生心跳
                .addLast(new IdleStateHandler(65, 0, 0))
                //消息处理
                .addLast(webSocketServerHandler);
    }
}
