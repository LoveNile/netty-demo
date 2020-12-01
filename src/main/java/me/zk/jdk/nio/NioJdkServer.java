package me.zk.jdk.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 使用jdk实现原生NIO
 */
public class NioJdkServer {

    //轮询
    private Selector selector;
    //服务端SocketChannel
    private ServerSocketChannel serverSocketChannel;
    //缓冲区 Buffer
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private int port;

    public NioJdkServer(int port) {
        try {
            this.port = port;
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            //设置非阻塞
            serverSocketChannel.configureBlocking(false);

            //注册事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            serverSocketChannel.socket();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        new NioJdkServer(8080).start();

    }

    public void start() throws Exception {
        while (true) {
            //先执行select
            selector.select();
            //获取OP_ACCEPT状态的key
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                doSomething(key);
            }
        }
//        selector.close();
//        serverSocketChannel.close();

    }

    public void doSomething(SelectionKey key) {
        try {
            //判断
            if (key.isAcceptable()) {
                ServerSocketChannel server = (ServerSocketChannel)key.channel();
                //这个方法体现非阻塞，不管你数据有没有准备好
                //你给我一个状态和反馈
                SocketChannel channel = server.accept();
                //一定一定要记得设置为非阻塞
                channel.configureBlocking(false);
                //当数据准备就绪的时候，将状态改为可读
                //改变状态后再注册上
                key = channel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                //key.channel 从多路复用器中拿到客户端的引用
                SocketChannel channel = (SocketChannel)key.channel();
                int len = channel.read(buffer);
                if(len > 0){
                    buffer.flip();
                    String content = new String(buffer.array(),0,len);
                    key = channel.register(selector,SelectionKey.OP_WRITE);
                    //在key上携带一个附件，一会再写出去
                    key.attach(content);
                    System.out.println("读取内容：" + content);
                }
            }
            if(key.isWritable()){
                SocketChannel channel = (SocketChannel)key.channel();
                String content = (String)key.attachment();
                channel.write(ByteBuffer.wrap(("输出：" + content).getBytes()));
                channel.close();
            }
        } catch (Exception e) {
        }
    }
}
