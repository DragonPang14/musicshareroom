package com.purejoy.musicshareroom.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.purejoy.musicshareroom.server.common.dto.ResultDto;
import com.purejoy.musicshareroom.server.common.enums.CustomizeStatusEnum;
import com.purejoy.musicshareroom.server.utils.ChatChannelUtils;
import com.purejoy.musicshareroom.server.utils.Constant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
@ChannelHandler.Sharable
@Slf4j
/**
 * @desc 用于处理接收到的数据
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * @author：Pang
     * @desc: while user join groupChat
     * @date: 2021/10/20
     * @param: [channelHandlerContext]
     * @return: void
     **/
    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext){
        Channel channel = channelHandlerContext.channel();
        channelGroup.writeAndFlush("welcome new friends:" + channel.remoteAddress());
        channelGroup.add(channel);
    }

    /**
     * @author：Pang
     * @desc: while user exit groupChat
     * @date: 2021/10/20
     * @param: [channelHandlerContext]
     * @return: void
     **/
    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + " user exit ");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        handlerWebSocketFrame(channelHandlerContext,webSocketFrame);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) {
        System.out.printf(channelHandlerContext.channel().id().asLongText());
        log.info(channelHandlerContext.channel().id().asLongText());
        //关闭连接
        if (webSocketFrame instanceof CloseWebSocketFrame){
            WebSocketServerHandshaker handshaker = Constant.webSocketHandshakerMap.get(channelHandlerContext.channel().id().asLongText());
            handshaker.close(channelHandlerContext.channel(),(CloseWebSocketFrame) webSocketFrame.retain());
            return;
        }

        //信息类型不为文本
        if (!(webSocketFrame instanceof TextWebSocketFrame)){
            sendErrorMessage(channelHandlerContext, CustomizeStatusEnum.ERROR_MESSAGE_TYPE);
            return;
        }

        String requestText = ((TextWebSocketFrame) webSocketFrame).text();
        log.info("服务端收到的新信息{}{}",channelHandlerContext.channel().id().asLongText(),requestText);


    }


    /**
     * @author：Pang
     * @desc: 发送错误信息
     * @date: 2021/9/26
     * @param: [channelHandlerContext, statusEnum]
     * @return: void
     **/
    private void sendErrorMessage(ChannelHandlerContext channelHandlerContext, CustomizeStatusEnum statusEnum){
        ResultDto<Object> errorResult = ResultDto.errorOf(statusEnum);
        String errorMessage = JSONObject.toJSONString(errorResult);
        ChatChannelUtils.sendMessage(channelHandlerContext,errorMessage);
    }
}
