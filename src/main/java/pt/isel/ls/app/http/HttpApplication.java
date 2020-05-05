package pt.isel.ls.app.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.app.Application;
import pt.isel.ls.router.Router;

public class HttpApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(HttpApplication.class);
    private static final int DEFAULT_PORT = 8080;

    public HttpApplication(Router router) {
        super(router);
    }

    @Override
    public void run() {
        String portEnv = System.getenv("PORT");
        int port = portEnv == null ? DEFAULT_PORT : Integer.parseInt(portEnv);

        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        AppServlet appServlet = new AppServlet(router);

        handler.addServletWithMapping(new ServletHolder(appServlet), "/*");

        server.setHandler(handler);

        try {
            server.start();
            LOG.info("Server listening on port {}", port);
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
