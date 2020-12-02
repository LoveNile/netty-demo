package me.zk.netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.zk.netty.chat.server.handler.ChatRoomInboundHandler;
import me.zk.netty.chat.server.handler.LoginInboundHandler;
import me.zk.netty.chat.server.handler.NettyServerInboundHandler;

public class NioNettyService {

    public static void main(String[] args) throws Exception {
        new NioNettyService().start();
    }
    public void start() throws Exception {
        //主从模式
        //默认线程为 CPU核心* 2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 引导类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("login", new LoginInboundHandler());
                            ch.pipeline().addLast("dealMsg",new NettyServerInboundHandler());
                            ch.pipeline().addLast("chatRoom", new ChatRoomInboundHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            System.out.println("服务开启！");
            future.channel().closeFuture().sync();
        } finally {
            //退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

