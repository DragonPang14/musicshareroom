package com.purejoy.musicshareroom.server;

import com.purejoy.musicshareroom.server.handler.NettyServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * netty客户端
 */
@Component
@Slf4j
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

    @Resource
    private NettyServerHandlerInitializer nettyServerHandlerInitializer;

    //netty Channel
    private Channel channel;


    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)//设置reactor线程
                .channel(NioServerSocketChannel.class)//指定nio类型为server
                .localAddress(nettyPort)//端口
                .option(ChannelOption.SO_BACKLOG,2014)//通道选项
                .childOption(ChannelOption.SO_KEEPALIVE, true)//keepalive
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(nettyServerHandlerInitializer);

        //启动netty，监听端口
        ChannelFuture future = serverBootstrap.bind().sync();
        if (future.isSuccess()) {
            channel = future.channel();
            log.info("[start][Netty Server 启动在 {} 端口]", nettyPort);
        }
    }

    @PreDestroy
    public void shutdown(){
        log.info("关闭netty 服务器");
        if (channel != null){
            channel.close();
        }
        Future<?> bossFuture = bossGroup.shutdownGracefully();
        Future<?> workFuture = workGroup.shutdownGracefully();
        try {
            bossFuture.await();
            workFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("netty服务器关闭成功");
    }
}
