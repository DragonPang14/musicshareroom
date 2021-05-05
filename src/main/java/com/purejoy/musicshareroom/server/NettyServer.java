package com.purejoy.musicshareroom.server;

import com.purejoy.musicshareroom.server.handler.NettyServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * netty客户端
 */
@Component
public class NettyServer {

    @Value("${netty.port}")
    private Integer nettyPort;

    /**
     * 连接组，用于接受连接
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * 工作组，用于接受读写
     */
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private NettyServerHandlerInitializer childChannelHandler;



    @PostConstruct
    public void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(nettyPort)
                .option(ChannelOption.SO_BACKLOG,2014)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(childChannelHandler);

    }
}
