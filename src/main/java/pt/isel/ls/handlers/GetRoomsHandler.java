package pt.isel.ls.handlers;

import pt.isel.ls.model.Room;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.util.Optional;

import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.console.TableView;

public class GetRoomsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets all of the Rooms or a specific room
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws Throwable Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        Optional<String> paramRid = request.getOptionalPathParameter("rid");
        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        if (paramRid.isPresent()) {
            int rid = Integer.parseInt(paramRid.get());
            Room room = new ConnectionProvider(dataSource)
                    .execute(conn -> new RoomQueries(conn).getRoom(rid));

            table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                    String.valueOf(room.getCapacity()), room.getDescription());
        } else {
            Iterable<Room> rooms = new ConnectionProvider(dataSource)
                    .execute(conn -> new RoomQueries(conn).getRooms());

            for (Room room : rooms) {
                table.addTableRow(String.valueOf(room.getRid()), room.getName(), room.getLocation(),
                        String.valueOf(room.getCapacity()), room.getDescription());
            }
        }

        return new RouteResponse(new TableView(table));
    }
}
