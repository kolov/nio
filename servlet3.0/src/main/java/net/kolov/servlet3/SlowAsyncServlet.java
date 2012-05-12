package net.kolov.servlet3;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**

 */

@WebServlet(urlPatterns = {"/hi"}, asyncSupported = true)
public class SlowAsyncServlet extends HttpServlet {
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(16);

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        final AsyncContext ac = request.startAsync();
        long time = 0L;
        if (request.getParameter("wait") != null) {
            time = Long.parseLong(request.getParameter("wait"));
        }

        final long finalTime = time;
        ses.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    ac.getResponse().getWriter().write("waited " + finalTime);
                } catch (IOException e) {
                    System.out.println("Error");
                }
                ac.complete();
            }
        }, finalTime, TimeUnit.MILLISECONDS);
    }


}
