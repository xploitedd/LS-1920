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
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt;
            ResultSet res;

            if (request.getOptionalPathParameter("rid").isPresent()) {
                int rid = Integer.parseInt(request.getOptionalPathParameter("rid").get());
                stmt = conn.prepareStatement("SELECT * FROM room WHERE rid = ?");
                stmt.setInt(1,rid);
            } else {
                stmt = conn.prepareStatement("SELECT * FROM room");
            }

            res = stmt.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            int size = metaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>(size);
            for (int i = 1; i <= size; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Table table = new Table(columnNames.toArray(String[]::new));
            while (res.next()) {
                int resRid = res.getInt(1);
                String name = res.getString(2);
                String location = res.getString(3);
                String desc = res.getString(4);
                int capacity = res.getInt(5);

                table.addTableRow(Integer.toString(resRid), name, location, desc, Integer.toString(capacity));
            }
            return new RouteResponse(new TableView(table));
        }
    }
}
