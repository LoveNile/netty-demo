package me.zk.netty.chat.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import me.zk.netty.chat.common.PubFunctions;

public class LoginInboundHandler extends ChannelInboundHandlerAdapter {

    private AttributeKey<String> loginSuccess = AttributeKey.newInstance("username");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断是否新注册的channel
        boolean isAuth = ctx.channel().hasAttr(loginSuccess);
        if (!isAuth) {
            String body = PubFunctions.getMsg((ByteBuf) msg);
            System.out.println("服务端：" + body + "登录成功！");
            ctx.channel().attr(loginSuccess);
            String returnMsg = "欢迎:" + body + "进入聊天室！";
            ByteBuf buf = PubFunctions.getBuffer(returnMsg);
            ctx.writeAndFlush(buf);
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}