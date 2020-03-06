package pt.isel.ls.db;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {

    private static void executeFile(String filePath) throws SQLException, IOException {
        Connection conn = Database.getInstance().getConnection();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            conn.prepareStatement(line).execute();
        }

        conn.close();
    }

    @BeforeClass
    public static void databaseTests_createSchema() throws SQLException, IOException {
        executeFile("src/test/sql/createSchema.sql");
        executeFile("src/test/sql/addData.sql");
    }

    @Test
    public void databaseTests_insert() throws SQLException {
        Connection conn = Database.getInstance().getConnection();
        assertEquals(1, conn.prepareStatement("INSERT INTO courses(name) VALUES ('LEIM');").executeUpdate());
        conn.close();
    }

    @Test
    public void databaseTests_delete() throws SQLException {
        Connection conn = Database.getInstance().getConnection();
        assertEquals(1, conn.prepareStatement("DELETE FROM students WHERE name LIKE 'A%'").executeUpdate());
        conn.close();
    }

    @Test
    public void databaseTests_select() throws SQLException {
        Connection conn = Database.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students");
        ResultSet rst = stmt.executeQuery();

        // check if there's 3 columns
        assertEquals(3, rst.getMetaData().getColumnCount());

        // check there is at least one student (there should be 2 or 1 depending on order of tests)
        assertTrue(rst.next());

        conn.close();
    }

    @Test
    public void databaseTests_update() throws SQLException {
        Connection conn = Database.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE students SET name = ? WHERE number = ?");
        stmt.setString(1, "Joana");
        stmt.setInt(2, 12346);
        assertEquals(1, stmt.executeUpdate());
    }
}
