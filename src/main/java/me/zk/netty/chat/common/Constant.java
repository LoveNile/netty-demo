package me.zk.netty.chat.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.List;

public class Constant {
    public static final AttributeKey<String> loginSuccess = AttributeKey.newInstance("username");

    public static final AttributeKey<List<ChannelHandlerContext>> loginUsers = AttributeKey.newInstance("loginUsers");
}
