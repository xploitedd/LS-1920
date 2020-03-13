package pt.isel.ls.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;
import pt.isel.ls.view.ExceptionView;

public class PostLabelHandler implements RouteHandler {
    private DataSource dataSource;
    public PostLabelHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            String n = request.getParameter("name").get(0);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
            stmt.setString(1,n);
            stmt.execute();
            PreparedStatement ret = conn.prepareStatement("SELECT lid FROM label WHERE name = ?;");
            stmt.setString(1,n);
            ResultSet rs = stmt.executeQuery();
            int lid = rs.getInt("lid"); //TODO: Return this somehow

            return new RouteResponse(null);
        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
