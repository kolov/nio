package kolov.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**


 */
public class SlowServlet extends HttpServlet {

    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(16);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long waitTime = parseWaitTime(request);
        longMethod(waitTime);

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("waited " + waitTime + "ms");
    }

    private long parseWaitTime(HttpServletRequest request) {
        long waitTime = 0L;
        if (request.getParameter("wait") != null) {
            waitTime = Long.parseLong(request.getParameter("wait"));
        }
        return waitTime;
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
            System.out.println("never happens");
        }

    }
}
