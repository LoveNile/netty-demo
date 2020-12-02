package me.zk.netty.chat.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import me.zk.netty.chat.common.PubFunctions;



public class NettyServerInboundHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String msg1 = PubFunctions.getMsg((ByteBuf)msg);
        System.out.println("服务器收到：" + msg1);
        String body = msg1 + System.currentTimeMillis();
        ByteBuf buf = PubFunctions.getBuffer(body);
        ctx.write(buf);
        ctx.fireChannelRead(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}