package me.zk.netty.chat.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;

public class PubFunctions {

    public static String getMsg(ByteBuf msg, String charset) throws UnsupportedEncodingException {
        ByteBuf buf = msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");
        return body;
    }
    public static String getMsg(ByteBuf msg) {
        ByteBuf buf = msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body =  new String(bytes);
        return body;
    }

    public static ByteBuf getBuffer(String msg) {
        byte[] bytes = msg.getBytes();
        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);
        return buf;
    }
    public static ByteBuf getBuffer(String msg, String charset) throws UnsupportedEncodingException {
        byte[] bytes = msg.getBytes(charset);
        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);
        return buf;
    }
}
