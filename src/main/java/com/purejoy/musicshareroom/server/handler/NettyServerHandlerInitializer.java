package com.purejoy.musicshareroom.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    private static final Integer READ_TIME_OUT = 3* 60;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new ReadTimeoutHandler(READ_TIME_OUT, TimeUnit.SECONDS));
    }
}
