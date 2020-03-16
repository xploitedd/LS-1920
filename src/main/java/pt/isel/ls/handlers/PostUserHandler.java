package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostUserHandler implements RouteHandler {

    private DataSource dataSource;

    public PostUserHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            String n = request.getParameter("name").get(0);
            String e = request.getParameter("email").get(0);
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO \"user\" (email,name) VALUES (?,?);"
            );
            stmt.setString(1,e);
            stmt.setString(2,n);
            stmt.execute();
            PreparedStatement ret = conn.prepareStatement(
                    "SELECT uid FROM label WHERE email = ? AND name = ?;"
            );
            ret.setString(1,e);
            ret.setString(2,n);
            ResultSet rs = ret.executeQuery();
            rs.first();
            int uid = rs.getInt("uid");
            return new RouteResponse(new IdentifierView("user",uid));
        }
    }

}
