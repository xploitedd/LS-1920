package pt.isel.ls.handlers;

import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;

import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomLabelQueries;
import pt.isel.ls.view.console.TableView;

public final class GetLabeledRoomsHandler implements RouteHandler {

    private final DataSource dataSource;

    public GetLabeledRoomsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Gets all of the rooms with a certain label
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */

    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        int lid = request.getPathParameter("lid").toInt();

        Iterable<Room> rooms = new ConnectionProvider(dataSource)
                .execute(conn -> new RoomLabelQueries(conn).getLabeledRooms(lid));

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        for (Room room : rooms) {
            table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                    String.valueOf(room.getCapacity()), room.getDescription());
        }

        return new RouteResponse(new TableView(table));
    }
}
