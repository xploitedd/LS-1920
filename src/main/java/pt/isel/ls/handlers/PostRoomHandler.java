package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.MessageView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class PostRoomHandler implements RouteHandler {

    private DataSource dataSource;

    public PostRoomHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            String n = request.getParameter("name").get(0);
            int c = Integer.parseInt(request.getParameter("capacity").get(0));
            String l = request.getParameter("location").get(0);

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO ROOMS (name, location, capacity) VALUES (?,?,?);"
            );

            stmt.setString(1,n);
            stmt.setString(2,l);
            stmt.setInt(3,c);
            stmt.execute();
            //Again, if you find an easier way tell me
            PreparedStatement ret = conn.prepareStatement(
                    "SELECT rid FROM ROOMS WHERE name = ? AND location = ? AND capacity = ?;"
            );

            ret.setString(1,n);
            ret.setString(2,l);
            ret.setInt(3,c);
            ResultSet rs = ret.executeQuery();
            rs.first();
            int rid = rs.getInt("rid");
            //Optional description
            Optional<List<String>> desc = request.getOptionalParameter("description");
            if (desc.isPresent()) {
                String d = desc.get().get(0);
                PreparedStatement din = conn.prepareStatement(
                        "INSERT INTO DESCRIPTION (rid, description) VALUES (?,?);"
                );
                din.setInt(1,rid);
                din.setString(2,d);
                din.execute();
            }

            //Fetch labels, check their IDs
            List<String> labels = request.getParameter("label");
            for (String lbl : labels) {
                PreparedStatement ls = conn.prepareStatement(
                        "SELECT lid FROM label WHERE name = ?;"
                );

                ls.setString(1,lbl);
                ResultSet rls = ls.executeQuery();
                rls.first();
                int lid = rls.getInt("lid");
                //insert rid-lid pairs into ROOM_LABEL
                PreparedStatement rl = conn.prepareStatement(
                        "INSERT INTO ROOM_LABEL (lid,rid) VALUES (?,?);"
                );

                rl.setInt(1,lid);
                rl.setInt(2,rid);
                rl.execute();
            }

            return new RouteResponse(new MessageView("This room's unique identifier is: " + rid));
        }
    }
}
