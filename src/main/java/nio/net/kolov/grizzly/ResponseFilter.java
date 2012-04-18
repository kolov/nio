package nio.net.kolov.grizzly;

import com.sun.grizzly.Context;
import com.sun.grizzly.ProtocolFilter;
import com.sun.grizzly.util.OutputWriter;
import com.sun.grizzly.util.WorkerThread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;


public class ResponseFilter implements ProtocolFilter {

    private String response = "HTTP/1.1 200 OK\n" +
            "Date: Tue, 27 Mar 2012 15:18:33 GMT\n" +
            "Server: Grizzly\n" +
            "Content-Language: en-US\n" +
            "Content-Length: 28\n" +
            "Connection: close\n" +
            "Content-Type: text/html;charset=UTF-8\n\n" +
            "<html><body>ok</body></html>";

    private ByteBuffer buf = ByteBuffer.wrap(response.getBytes());//.allocateDirect(response.length());


    public boolean execute(Context ctx) throws IOException {
        final WorkerThread workerThread = ((WorkerThread) Thread.currentThread());
        ByteBuffer buffer = workerThread.getByteBuffer();

        buffer.flip();
        if (buffer.hasRemaining()) {
            SelectableChannel channel = ctx.getSelectionKey().channel();
            OutputWriter.flushChannel(channel, buf);
            channel.close();
        }

        buffer.clear();
        return false;
    }

    public boolean postExecute(Context ctx) throws IOException {
        return true;
    }
}
