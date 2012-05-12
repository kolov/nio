package net.kolov.grizzly;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * http://grizzly.java.net/nonav/docs/docbkx2.0/html/httpserverframework-samples.html
 */
public class EchoServer {


    public static void main(String[] args) throws IOException {
        final HttpServer server = HttpServer.createSimpleServer();

        final ServerConfiguration config = server.getServerConfiguration();

        // Map the path, /echo, to the NonBlockingEchoHandler
        config.addHttpHandler(new NonBlockingEchoHandler(), "/echo");

        try {
            server.start();
            System.out.println("Press ENTER to stop");
            System.in.read();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
    }
}