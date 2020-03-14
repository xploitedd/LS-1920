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

import pt.isel.ls.view.TableView;

public class GetUserBookingsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetUserBookingsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets bookings booked by a user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws Throwable Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            int uid = Integer.parseInt(request.getPathParameter("uid"));
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BOOKING WHERE uid = ?");
            stmt.setInt(1, uid);
            ResultSet res = stmt.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            int size = metaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
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
