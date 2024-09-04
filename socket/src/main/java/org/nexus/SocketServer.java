package org.nexus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Xieningjun
 * @date 2024/8/30 14:56
 * @description
 */
public class SocketServer {

    public static void main(String[] args) {
        try {
            // 创建服务器端 Socket 并监听端口
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server is listening on port 8080");

            // 等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // 获取输入流并读取客户端发送的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = in.readLine();
            System.out.println("Received from client: " + message);

            // 获取输出流并发送响应
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello, Client!");

            // 关闭流和 Socket
            in.close();
            out.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
