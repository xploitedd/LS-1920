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

public class GetLabelsHandler implements RouteHandler {

    private DataSource dataSource;

    public GetLabelsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets all existing Labels
     * @param request Request information
     * @return routeResponse
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM LABEL");
            ResultSet res = stmt.executeQuery();
            ResultSetMetaData metaData = res.getMetaData();
            int size = metaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Table table = new Table(columnNames.toArray(String[]::new));
            while (res.next()) {
                int lid = res.getInt(1);
                String name = res.getString(2);

                table.addTableRow(Integer.toString(lid), name);
            }
            return new RouteResponse(new TableView(table));
        } catch (SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
