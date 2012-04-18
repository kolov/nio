package nio.net.kolov.nio;

import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EchoWorker implements Runnable {
    private List queue = new LinkedList();

    public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {

        if (count == -1) {
            return;
        }
        System.out.println("Data received: " + count + ":\n" + new String(data, 0, count));
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        String dateFormatted = format.format(new Date());
        String content = "<html><body>You said: <pre>" + new String(data, 0, count) + " </pre></body></html>";
        String response = "HTTP/1.1 200 OK\n" +
                "Date: " + dateFormatted + "\n" +
                "Server: NIOServer\n" +
                "Content-Length: " + content.length() + "\n" +
                "Content-Type: text/html;charset=UTF-8\n\n" +
                content;


        synchronized (queue) {
            queue.add(new ServerDataEvent(server, socket, response.getBytes()));
            queue.notify();
        }
    }

    public void run() {
        ServerDataEvent dataEvent;

        while (true) {
            // Wait for data to become available
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                    }
                }
                dataEvent = (ServerDataEvent) queue.remove(0);
            }

            // Return to sender
            dataEvent.server.send(dataEvent.socket, dataEvent.data);
        }
    }

    private static class ServerDataEvent {
        public NioServer server;
        public SocketChannel socket;
        public byte[] data;

        public ServerDataEvent(NioServer server, SocketChannel socket, byte[] data) {
            this.server = server;
            this.socket = socket;
            this.data = data;
        }
    }
}