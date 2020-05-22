package pt.isel.ls.app.http;

import org.eclipse.jetty.server.Server;
import pt.isel.ls.exceptions.AppException;

import java.util.HashSet;

public class HttpPool {

    private final HashSet<Server> pool = new HashSet<>();

    public void addServer(Server server) {
        pool.add(server);
    }

    public void terminate() {
        try {
            for (Server server : pool) {
                if (server.isRunning()) {
                    server.stop();
                    pool.remove(server);
                }
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

}
