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

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        log.info("welcome new friends on channelActive:{}",channel.remoteAddress());
        channelGroup.add(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("goodbye friends on channelInactive:{}",ctx.channel().remoteAddress());
        channelGroup.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常信息抛出:{},异常channel,{}",cause.getLocalizedMessage(),ctx.channel().id());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) throws Exception {
        handlerWebSocketFrame(ctx,webSocketFrame);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        log.info(ctx.channel().id().asLongText());
        //关闭连接
        if (webSocketFrame instanceof CloseWebSocketFrame){
            WebSocketServerHandshaker handshaker = Constant.webSocketHandshakerMap.get(ctx.channel().id().asLongText());
            handshaker.close(ctx.channel(),(CloseWebSocketFrame) webSocketFrame.retain());
            return;
        }

        //信息类型不为文本
        if (!(webSocketFrame instanceof TextWebSocketFrame)){
            sendErrorMessage(ctx, CustomizeStatusEnum.ERROR_MESSAGE_TYPE);
            return;
        }

        String requestText = ((TextWebSocketFrame) webSocketFrame).text();
        log.info("服务端收到的新信息{}{}",ctx.channel().id().asLongText(),requestText);


    }


    /**
     * @author：Pang
     * @desc: 发送错误信息
     * @date: 2021/9/26
     * @param: [ctx, statusEnum]
     * @return: void
     **/
    private void sendErrorMessage(ChannelHandlerContext ctx, CustomizeStatusEnum statusEnum){
        ResultDto<Object> errorResult = ResultDto.errorOf(statusEnum);
        String errorMessage = JSONObject.toJSONString(errorResult);
        ChatChannelUtils.sendMessage(ctx,errorMessage);
    }
}
