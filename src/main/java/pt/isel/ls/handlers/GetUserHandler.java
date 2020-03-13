package pt.isel.ls.handlers;

import pt.isel.ls.model.Table;
import pt.isel.ls.router.RequestParameters;
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

public class GetUserHandler implements RouteHandler {

    private DataSource dataSource;

    public GetUserHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets user information
     * @param request The route request
     * @return
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            int uid = Integer.parseInt(request.getPathParameter("uid"));
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USER WHERE uid = ?");
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
                int resUid = res.getInt(1);
                String email = res.getString(2);
                String name = res.getString(3);

                table.addTableRow(Integer.toString(resUid), email, name);
            }

            return new RouteResponse(new TableView(table));
        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
