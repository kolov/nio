package nio.net.kolov.grizzly;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * User: assenkolov
 * Date: 3/27/12
 * Time: 5:32 PM
 */
public class WebServer {

    private static String response = "HTTP/1.1 200 OK\n" +
            "Date: Tue, 27 Mar 2012 15:18:33 GMT\n" +
            "Server: Grizzly\n" +
            "Content-Language: en-US\n" +
            "Content-Length: 28\n" +
            "Content-Type: text/html;charset=UTF-8\n\n" +
            "<html><body>ok</body></html>";


    public static void main(String[] args) throws Exception {
        final GrizzlyWebServer ws = new GrizzlyWebServer(9002);
        ws.addGrizzlyAdapter(new GrizzlyAdapter() {
            @Override
            public void service(GrizzlyRequest grizzlyRequest, final GrizzlyResponse grizzlyResponse) {

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            grizzlyResponse.getOutputBuffer().write(response);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (InterruptedException e) {

                        }
                    }
                });


            }
        });
        ws.start();

        // final WSServlet wsServlet = new WSServlet();
        // final ServletAdapter wsa = new ServletAdapter(wsServlet);
        // wsa.addServletListener("com.sun.xml.ws.transport.http.servlet.WSServletContextListener");
        // ws.addGrizzlyAdapter(wsa, new String[]{"/ws"});
        // ws.start();
    }
}
