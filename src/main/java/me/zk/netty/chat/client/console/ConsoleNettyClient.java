package me.zk.netty.chat.client.console;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.zk.netty.chat.client.console.handler.ConsoleInboundHandler;

import java.util.Scanner;

public class ConsoleNettyClient {
    private String username;

    public ConsoleNettyClient(String username) {
        this.username = username;
    }

    public static void main(String[] args) throws Exception {
        String username = "";
        int port = 8080;
        String ip = "127.0.0.1";
        if (args.length >0 ) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        System.out.println("请输入用户名!!");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            username = scanner.nextLine();
        } else {
            System.out.println("未输入用户名");
            System.exit(1);
        }

        new ConsoleNettyClient(username).consoleClientStart(8080, ip);
    }

    public void consoleClientStart(int port,String ip) throws Exception {
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
                            socketChannel.pipeline().addLast(new ConsoleInboundHandler(username));
                        }
                    });
            ChannelFuture future = client.connect(ip,port).sync();
            System.out.println("客户端开启！");
            future.channel().closeFuture().sync();
        } finally {
            work.shutdownGracefully();
        }


    }
}
