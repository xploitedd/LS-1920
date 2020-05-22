package pt.isel.ls.app.http;

import org.eclipse.jetty.server.Server;
import pt.isel.ls.exceptions.AppException;

import java.util.HashMap;

public class HttpPool {

    private final HashMap<Integer, Server> pool = new HashMap<>();

    /**
     * Adds a server instance to the server pool
     * @param port port where the instance is running
     * @param server server instance
     */
    public void addServer(int port, Server server) {
        pool.put(port, server);
    }

    /**
     * Terminate all running server instances
     */
    public void terminate() {
        for (int port : pool.keySet()) {
            terminate(port);
        }
    }

    /**
     * Terminate a specific server instance
     * @param port port where the server is running
     */
    public void terminate(int port) {
        Server server = pool.get(port);
        if (server == null) {
            throw new AppException("There is no server running on port " + port);
        }

        try {
            if (server.isRunning()) {
                server.stop();
                pool.remove(port);
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

}
