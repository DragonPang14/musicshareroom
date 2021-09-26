package com.purejoy.musicshareroom.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.purejoy.musicshareroom.common.dto.ResultDto;
import com.purejoy.musicshareroom.common.enums.CustomizeStatusEnum;
import com.purejoy.musicshareroom.utils.Constant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
/**
 * @desc 用于处理接收到的数据
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


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
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(errorMessage));
    }
}
