package pt.isel.ls.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.util.ArrayList;

import pt.isel.ls.view.TableView;

public class GetLabeledRoomsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetLabeledRoomsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets all of the rooms with a certain label
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws Throwable
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            int lid = Integer.parseInt(request.getPathParameter("lid"));
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM ROOM WHERE rid IN (SELECT rid FROM ROOM_LABEL WHERE lid = ?)"
            );

            stmt.setInt(1, lid);
            ResultSet res = stmt.executeQuery();
            ResultSetMetaData metaData = res.getMetaData();
            int size = metaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Table table = new Table(columnNames.toArray(String[]::new));
            while (res.next()) {
                int rid = res.getInt(1);
                String name = res.getString(2);
                String location = res.getString(3);
                int capacity = res.getInt(4);

                table.addTableRow(Integer.toString(rid),name,location,Integer.toString(capacity));
            }
            return new RouteResponse(new TableView(table));
        }
    }
}
