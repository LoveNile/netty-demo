package me.zk.netty.chat.client.console;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.zk.netty.chat.client.console.handler.ConsoleInboundHandler;

public class ConsoleNettyClient {
    
    public static void main(String[] args) throws Exception {
        new ConsoleNettyClient().consoleClientStart();
    }

    public void consoleClientStart() throws Exception {
        //引导类
        Bootstrap client = new Bootstrap();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            client.group(work).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ConsoleInboundHandler());
                        }
                    });
            ChannelFuture future = client.connect("127.0.0.1",8080).sync();
            System.out.println("客户端开启！");
            future.channel().closeFuture().sync();
        } finally {
            work.shutdownGracefully();
        }


    }
}
