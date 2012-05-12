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
        System.out.println("Received: " + count + " bytes from socketChannel " + System.identityHashCode(socket));

        boolean readyToSend = false;
        ServerResponse response = server.getResponse(socket);
        String dataString = new String(data, 0, count);
        if (response.getContent().length() == 0 && !dataString.startsWith("GET /test HTTP")) {
            response.fail();
            readyToSend = true;
        } else {
            // good request, go on
            response.addContent(data, count);
        }

        if (response.getContent().indexOf("\r\n\r\n") != -1) {
            readyToSend = true;
        }
        if (readyToSend) {
            // all headers in!
            synchronized (queue) {
                byte[] responseBytes = response.getBytes();
                server.dismissResponse(socket);
                queue.add(new ServerDataEvent(server, socket, responseBytes));
                queue.notify();
            }
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