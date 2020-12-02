package me.zk.netty.chat.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.text.SimpleDateFormat;

public class NettyServerInboundHandler extends ChannelInboundHandlerAdapter {

    private AttributeKey<String> loginSuccess = AttributeKey.newInstance("username");
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        System.out.println(System.currentTimeMillis());
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body =  new String(bytes, "UTF-8");
        System.out.println("服务器收到：" + body);
        boolean islogin = ctx.channel().hasAttr(loginSuccess);
        if (!islogin) {
            ctx.channel().attr(loginSuccess);
            String returnMsg = "欢迎:" + body+ "进入聊天室！";
            byte[] r = returnMsg.getBytes();
            ByteBuf buf1 = Unpooled.buffer(r.length);
            buf1.writeBytes(r);
            ctx.writeAndFlush(buf1);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}