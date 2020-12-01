package me.zk.jdk.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class BIOClient {

	public static void main(String[] args) throws UnknownHostException, IOException {

		//要和谁进行通信，服务器IP、服务器的端口
		//一台机器的端口号是有限
		Socket client = new Socket("localhost", 8080);

		//输出 O  write();
		//不管是客户端还是服务端，都有可能write和read

		OutputStream os = client.getOutputStream();

		//生成一个随机的ID
		String name = UUID.randomUUID().toString();

		System.out.println("客户端发送数据：" + name);
		//传说中的101011010
		os.write(name.getBytes());
//
//		InputStream in = client.getInputStream();
//		byte[] bytes = new byte[1024];
//		in.read(bytes);
//		System.out.println(new String(bytes,"UTF-8"));

		os.close();
		client.close();

		
	}
	
}
