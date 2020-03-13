package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;

public class PostUserHandler implements RouteHandler {

    private DataSource dataSource;

    public PostUserHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) {
        return new RouteResponse(null);
    }

}
