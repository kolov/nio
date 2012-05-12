package net.kolov.grizzly;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.io.NIOReader;
import org.glassfish.grizzly.http.server.io.NIOWriter;
import org.glassfish.grizzly.http.server.io.ReadHandler;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * http://grizzly.java.net/nonav/docs/docbkx2.0/html/httpserverframework-samples.html
 * <p/>
 * Date: 5/9/12
 */
public class NonBlockingEchoHandler extends HttpHandler {


    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(16);


    // -------------------------------------------- Methods from HttpHandler

    private static int count = 0;

    @Override
    public void service(final Request request,
                        final Response response) throws Exception {

        final char[] buf = new char[128];
        final NIOReader in = request.getReader(false); // put the stream in non-blocking mode
        final NIOWriter out = response.getWriter();

        long waitTime = 0;
        if (request.getParameter("wait") != null) {
            waitTime = Long.parseLong(request.getParameter("wait"));
        }

        response.suspend();

        final long finalWaitTime = waitTime;
        in.notifyAvailable(new ReadHandler() {

            @Override
            public void onDataAvailable() throws Exception {
                echoAvailableData(in, out, buf);
                in.notifyAvailable(this);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("[onError]" + t);
                response.resume();
            }


            @Override
            public void onAllDataRead() throws Exception {
                final int taskId = count++;

                //      System.out.println("" + System.currentTimeMillis() + ": schedule " + taskId + " in " + finalWaitTime + "ms");
                ses.schedule(new Runnable() {
                    @Override
                    public void run() {
                        //    System.out.println("" + System.currentTimeMillis() + ":running " + taskId );
                        try {
                            out.write("waited " + finalWaitTime + "ms");
                        } catch (IOException e) {
                            System.out.println("Error");
                            return;
                        } finally {
                            try {
                                in.close();
                            } catch (IOException ignored) {
                            }

                            try {
                                out.close();
                            } catch (IOException ignored) {
                            }

                            response.resume();

                        }
                    }
                }, finalWaitTime, TimeUnit.MILLISECONDS);
            }
        }

        );

    }

    private void echoAvailableData(NIOReader in, NIOWriter out, char[] buf)
            throws IOException {

        while (in.isReady()) {
            int len = in.read(buf);
            out.write(buf, 0, len);
        }

    }


} // END NonBlockingEchoHandler