package netty.learn.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Xieningjun
 * @date 2024/5/28 19:12
 */
public class BioEchoServer {

    private static class EchoClientHandler extends Thread {

        private Socket socket;

        public EchoClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (InputStream input = socket.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 OutputStream output = socket.getOutputStream();
                 PrintWriter writer = new PrintWriter(output, true)) {

                String text;
                while ((text = reader.readLine()) != null) {
                    System.out.println("Received: " + text);
                    writer.println("Echo: " + text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new EchoClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
