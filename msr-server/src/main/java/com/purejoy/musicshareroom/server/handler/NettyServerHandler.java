package com.purejoy.musicshareroom.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        handlerFullHttpRequest(channelHandlerContext,o);
    }

    private void handlerFullHttpRequest(ChannelHandlerContext channelHandlerContext, Object o) {

    }
}
