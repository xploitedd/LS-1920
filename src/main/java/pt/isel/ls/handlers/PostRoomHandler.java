package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
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

            //Fetch labels, check their IDs
            Optional<List<String>> labels = request.getOptionalParameter("label");
            List<Integer> lids = new LinkedList<>();
            if (labels.isPresent()) {
                for (String lbl : labels.get()) {
                    PreparedStatement ls = conn.prepareStatement(
                            "SELECT lid FROM label WHERE name = ?;"
                    );

                    ls.setString(1, lbl);
                    ResultSet rls = ls.executeQuery();
                    int lid = -1;
                    if (rls.next()) {
                        rls.getInt("lid");
                    } else {
                        throw new IllegalArgumentException("Label '" + lbl + "' not found, aborted creation of room");
                    }
                    lids.add(lid);
                }
            }
            String n = request.getParameter("name").get(0);
            int c = Integer.parseInt(request.getParameter("capacity").get(0));
            String l = request.getParameter("location").get(0);

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO room (name, location, capacity) VALUES (?,?,?);"
            );

            stmt.setString(1, n);
            stmt.setString(2, l);
            stmt.setInt(3, c);
            stmt.execute();
            //Again, if you find an easier way tell me
            PreparedStatement ret = conn.prepareStatement(
                    "SELECT rid FROM room WHERE name = ? AND location = ? AND capacity = ?;"
            );

            ret.setString(1, n);
            ret.setString(2, l);
            ret.setInt(3, c);
            ResultSet rs = ret.executeQuery();
            rs.next();
            int rid = rs.getInt("rid");
            //Optional description
            Optional<List<String>> desc = request.getOptionalParameter("description");
            if (desc.isPresent()) {
                String d = desc.get().get(0);
                PreparedStatement din = conn.prepareStatement(
                        "INSERT INTO description (rid, description) VALUES (?,?);"
                );
                din.setInt(1, rid);
                din.setString(2, d);
                din.execute();
            }

            for (int lid : lids) {
                //insert rid-lid pairs into ROOM_LABEL
                PreparedStatement rl = conn.prepareStatement(
                        "INSERT INTO room_label (lid,rid) VALUES (?,?);"
                );

                rl.setInt(1, lid);
                rl.setInt(2, rid);
                rl.execute();
            }

            return new RouteResponse(new IdentifierView("room", rid));
        }
    }
}
