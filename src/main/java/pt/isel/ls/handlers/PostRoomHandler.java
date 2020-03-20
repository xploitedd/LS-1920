package pt.isel.ls.handlers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;

public class PostRoomHandler implements RouteHandler {

    private DataSource dataSource;

    public PostRoomHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        Optional<List<String>> optLabels = request.getOptionalParameter("label");
        String desc = request.getOptionalParameter("description")
                .map(strings -> strings.get(0)).orElse(null);;

        String name = request.getParameter("name").get(0);
        int capacity = Integer.parseInt(request.getParameter("capacity").get(0));
        String location = request.getParameter("location").get(0);

        Room inserted = new ConnectionProvider(dataSource).execute(conn -> {
            List<Label> labels = new LinkedList<>();
            if (optLabels.isPresent()) {
                LabelQueries labelQueries = new LabelQueries(conn);
                for (String label : optLabels.get())
                    labels.add(labelQueries.getLabel(label));
            }

            return new RoomQueries(conn).createNewRoom(name, location, capacity, desc, labels);
        });

        return new RouteResponse(new IdentifierView("room",inserted.getRid()));

    }
}
