package pt.isel.ls.handlers;

import pt.isel.ls.router.RequestParameters;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.ExceptionView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostRoomHandler implements RouteHandler {
    private DataSource dataSource;
    public PostRoomHandler(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public RouteResponse execute(RouteRequest request) {
        try (Connection conn = dataSource.getConnection()) {
            // name - the rooms's name.
            //description - the rooms's description.
            //location - the room's location.
            //label - the set of labels for the room.
            String n = request.getParameter("name").get(0);
            Optional<List<String>> desc = request.getOptionalParameter("description");
            if (desc.isPresent()) {
                String d = desc.get().get(0);
            } else {
                //TODO: Handle room descriptions
            }
            String l = request.getParameter("location").get(0);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
            stmt.setString(1,n);
            stmt.execute();
            PreparedStatement ret = conn.prepareStatement("SELECT lid FROM label WHERE name = ?;");
            stmt.setString(1,n);
            ResultSet rs = stmt.executeQuery();
            int lid = rs.getInt("lid"); //TODO: Return this somehow

            return null;
        } catch (RequestParameters.ParameterNotFoundException | SQLException e) {
            return new RouteResponse(new ExceptionView(e)).setStatusCode(500);
        }
    }
}
