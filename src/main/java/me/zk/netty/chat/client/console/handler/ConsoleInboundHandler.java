package me.zk.netty.chat.client.console.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.zk.netty.chat.common.PubFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ConsoleInboundHandler extends ChannelInboundHandlerAdapter {
    private String username;

    public ConsoleInboundHandler(String username) {
        this.username = username;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = PubFunctions.getBuffer(username);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = PubFunctions.getMsg((ByteBuf)msg);
        System.out.println("客户端收到：" + body);
        Scanner scanner = new Scanner(System.in);
        if(scanner.hasNextLine()) {
            String a = scanner.nextLine();
            ByteBuf buf = PubFunctions.getBuffer(a);
            ctx.writeAndFlush(buf);
            System.out.println("客户端发送：" + a);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
