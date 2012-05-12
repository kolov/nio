package kolov.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.*;

/**


 */
public class SlowServlet extends HttpServlet {

    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(16);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        long time = 0L;
        if (request.getParameter("wait") != null) {
            time = Long.parseLong(request.getParameter("wait"));
        }
        longMethod(time);
        out.println("waited " + time + "ms");
    }


    private void longMethod(long time) {
        final Semaphore m = new Semaphore(0);

        ses.schedule(new Runnable() {
            @Override
            public void run() {
                m.release();
            }
        }, time, TimeUnit.MILLISECONDS);


        try {
            m.acquire();
        } catch (InterruptedException e) {
            // ok
            System.out.println("Interrupted");
        }

    }
}
