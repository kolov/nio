package nio.net.kolov.nio;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple server response. Returns only HTTP 200 echoing request headers or HTTP 500
 */
public class ServerResponse {
    private String content = "";
    private boolean success = true;

    public byte[] getBytes() {
        if (success) {
            String dateFormatted = makeFormattedDate();

            String body = "<html><body>You said: <pre>" + content + " </pre></body></html>";
            String r = "HTTP/1.1 200 OK\n" +
                    "Date: " + dateFormatted + "\n" +
                    "Server: NIOServer\n" +
                    "Content-Length: " + body.length() + "\n" +
                    "Content-Type: text/html;charset=UTF-8\n\n" +
                    body;
            return r.getBytes();
        } else {
            return "HTTP/1.1 500\r\n\r\n".getBytes();
        }
    }

    private String makeFormattedDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        return format.format(new Date());
    }

    public synchronized void addContent(byte[] data, int count) {
        content = content + new String(data, 0, count);
    }

    public String getContent() {
        return content;
    }

    public void fail() {
        success = false;
    }
}
