package com.purejoy.musicshareroom.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.purejoy.musicshareroom.server.service.ChatService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: Pang
 * @desc: 聊天服务层
 * @date: 2021/9/28 23:30
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {

    }
}
