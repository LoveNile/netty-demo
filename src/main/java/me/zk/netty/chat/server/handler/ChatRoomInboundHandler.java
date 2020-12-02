package me.zk.netty.chat.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import me.zk.netty.chat.common.Constant;
import me.zk.netty.chat.common.PubFunctions;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        if (!channel.hasAttr(Constant.loginUsers)) {

        } else {
            int size = channel.attr(Constant.loginUsers).get().size();
            String body = "在线人数：" + size;
            ByteBuf buf = PubFunctions.getBuffer(body);
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
