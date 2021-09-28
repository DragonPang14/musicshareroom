package com.purejoy.musicshareroom.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author: Pang
 * @desc: 聊天w通信channel工具类
 * @date: 2021/9/28 23:55
 */
public class ChatChannelUtils {

    /**
     * @author：Pang
     * @desc: 发送消息
     * @date: 2021/9/28
     * @param: [channelHandlerContext, message]
     * @return: void
     **/
    public static void sendMessage(ChannelHandlerContext channelHandlerContext, String message){
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(message));
    }
}
