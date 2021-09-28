package com.purejoy.musicshareroom.server.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: Pang
 * @desc:
 * @date: 2021/9/28 23:29
 */
public interface ChatService {

    /**
     * @desc 私聊发送信息
     * @param param
     * @param ctx
     */
    void singleSend(JSONObject param, ChannelHandlerContext ctx);
}
