package com.purejoy.musicshareroom.server.handler;

import com.purejoy.musicshareroom.utils.Constant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        handlerWebSocketFrame(channelHandlerContext,webSocketFrame);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) {
        System.out.printf(channelHandlerContext.channel().id().asLongText());
        //关闭连接
        if (webSocketFrame instanceof CloseWebSocketFrame){
            WebSocketServerHandshaker handshaker = Constant.webSocketHandshakerMap.get(channelHandlerContext.channel().id().asLongText());
            handshaker.close(channelHandlerContext.channel(),(CloseWebSocketFrame) webSocketFrame.retain());
            return;
        }

        if (!(webSocketFrame instanceof TextWebSocketFrame)){
            return;
        }


    }
}
