package pt.isel.ls.app.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.app.Application;
import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.router.Router;

public class HttpApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(HttpApplication.class);

    private final HttpPool httpPool;
    private final int port;

    public HttpApplication(HttpPool httpPool, Router router, int port) {
        super(router);
        this.httpPool = httpPool;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Server server = new Server(port);
            ServletHandler handler = new ServletHandler();
            AppServlet appServlet = new AppServlet(router);

            handler.addServletWithMapping(new ServletHolder(appServlet), "/*");

            server.setHandler(handler);
            server.setErrorHandler(new HttpErrorHandler(router));
            server.start();
            LOG.info("Server listening on port {}", port);
            httpPool.addServer(port, server);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

}
