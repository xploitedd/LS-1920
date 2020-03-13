package pt.isel.ls.handlers;

import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import pt.isel.ls.view.ExceptionView;
import pt.isel.ls.view.TableView;

public class GetRoomsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetRoomsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets Rooms or a specific room
     * @param request The route request
     * @return
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt;
            ResultSet res;

            if (request.getOptionalPathParameter("rid").isPresent()) {
                int rid = Integer.parseInt(request.getOptionalPathParameter("rid").get());
                stmt = conn.prepareStatement("SELECT * FROM ROOM WHERE rid = ?");
                stmt.setInt(1,rid);
            } else {
                stmt = conn.prepareStatement("SELECT * FROM ROOMS");
            }

            res = stmt.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            int size = metaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Table table = new Table(columnNames.toArray(String[]::new));
            while (res.next()) {
                int resRid = res.getInt(1);
                String name = res.getString(2);
                String location = res.getString(3);
                int capacity = res.getInt(4);

                table.addTableRow(Integer.toString(resRid), name, location, Integer.toString(capacity));
            }
            return new RouteResponse(new TableView(table));
        } catch (SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
