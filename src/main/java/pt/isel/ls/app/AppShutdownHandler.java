package pt.isel.ls.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.app.http.HttpPool;

public class AppShutdownHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(AppShutdownHandler.class);

    private final HttpPool httpPool;

    public AppShutdownHandler(HttpPool httpPool) {
        this.httpPool = httpPool;
    }

    @Override
    public void run() {
        log.info("Running shutdown hook!");
        httpPool.terminate();
        log.info("Shutdown hook terminated!");
    }

}
