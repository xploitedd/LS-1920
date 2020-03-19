package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import pt.isel.ls.model.Label;

public class LabelQueries extends DatabaseQueries {

    public LabelQueries(Connection conn) {
        super(conn);
    }

    public Label createNewLabel(String labelName) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, labelName);
        stmt.execute();

        return getLabel(labelName);
    }

    public Label getLabel(String name) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement("SELECT lid FROM label WHERE name = ?;");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new NoSuchElementException("Label '" + name + "' not found");
        }
        return new Label(rs.getInt("lid"), name);
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
