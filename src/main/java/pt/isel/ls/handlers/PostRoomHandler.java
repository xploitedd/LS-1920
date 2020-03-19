package pt.isel.ls.handlers;

import pt.isel.ls.model.Room;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostRoomHandler implements RouteHandler {

    private DataSource dataSource;

    public PostRoomHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {

        Optional<List<String>> labels = request.getOptionalParameter("label");

        String name = request.getParameter("name").get(0);
        int capacity = Integer.parseInt(request.getParameter("capacity").get(0));
        String location = request.getParameter("location").get(0);
        Optional<List<String>> desc = request.getOptionalParameter("description");

        Room inserted = new ConnectionProvider(dataSource)
                .execute(conn -> new RoomQueries(conn).createNewRoom(name, location, capacity, desc, labels));

        return new RouteResponse(new IdentifierView("room",inserted.getRid()));

    }
}
