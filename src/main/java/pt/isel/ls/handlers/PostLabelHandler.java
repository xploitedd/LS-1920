package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostLabelHandler implements RouteHandler {

    private DataSource dataSource;

    public PostLabelHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            String n = request.getParameter("name").get(0);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
            stmt.setString(1,n);
            stmt.execute();

            PreparedStatement ret = conn.prepareStatement("SELECT lid FROM label WHERE name = ?;");
            ret.setString(1,n);
            ResultSet rs = ret.executeQuery();
            rs.first();

            int lid = rs.getInt("lid");
            return new RouteResponse(new IdentifierView("label",lid));
        }
    }
}
