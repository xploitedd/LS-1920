package pt.isel.ls.handlers;

import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;

import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.console.TableView;

public class GetLabeledRoomsHandler implements RouteHandler {

    private static final String NO_DESCRIPTION = "No Description";
    private DataSource dataSource;

    public GetLabeledRoomsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Gets all of the rooms with a certain label
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws Throwable Sent to the router
     */

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        int lid = Integer.parseInt(request.getPathParameter("lid"));

        Iterable<Room> rooms = new ConnectionProvider(dataSource)
                .execute(conn -> new RoomLabelQueries(conn).getLabeledRooms(lid));

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");

        for (Room room : rooms) {
            String desc = room.getDescription();
            table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                    String.valueOf(room.getCapacity()), desc != null ? desc : "NO DESCRIPTION");
        }
        return new RouteResponse(new TableView(table));
    }
}
