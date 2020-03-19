package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import pt.isel.ls.model.Label;

public class LabelQueries extends DatabaseQueries {

    public LabelQueries(Connection conn) {
        super(conn);
    }

    public Label createNewLabel(String labelName) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, labelName);
        stmt.execute();

        stmt = conn.prepareStatement("SELECT lid FROM label WHERE name = ?;");
        stmt.setString(1, labelName);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return new Label(rs.getInt("lid"), labelName);
    }

    public Iterable<Label> getLabels() throws Throwable {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM label");
        ResultSet rs = stmt.executeQuery();

        LinkedList<Label> results = new LinkedList<>();
        while (rs.next()) {
            int lid = rs.getInt("lid");
            String name = rs.getString("name");
            results.add(new Label(lid, name));
        }

        return results;
    }

}
