package pt.isel.ls.handlers;

import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import pt.isel.ls.view.console.TableView;

public class GetRoomBookingsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomBookingsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets Bookings from Rooms
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws Throwable Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            int rid = Integer.parseInt(request.getPathParameter("rid"));
            PreparedStatement stmt;

            if (request.getOptionalPathParameter("bid").isPresent()) {
                int bid = Integer.parseInt(request.getOptionalPathParameter("bid").get());
                stmt = conn.prepareStatement("SELECT * FROM booking WHERE rid = ? AND bid = ?");
                stmt.setInt(1, rid);
                stmt.setInt(2, bid);
            } else {
                stmt = conn.prepareStatement("SELECT * FROM booking WHERE rid = ?");
                stmt.setInt(1, rid);
            }

            ResultSet res = stmt.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            int size = metaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>(size);
            for (int i = 1; i <= size; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Table table = new Table(columnNames.toArray(String[]::new));
            while (res.next()) {
                int bid = res.getInt(1);
                String begin = res.getTimestamp(2).toString();
                String end = res.getTimestamp(3).toString();
                int resRid = res.getInt(4);
                int resUid = res.getInt(5);

                table.addTableRow(Integer.toString(bid), begin, end, Integer.toString(resRid),
                        Integer.toString(resUid));
            }
            return new RouteResponse(new TableView(table));
        }
    }
}
