package pt.isel.ls;

import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_TEST_DATABASE_URL";
    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    private static void executeFile(String filePath) throws SQLException, IOException {
        Connection conn = dataSource.getConnection();
        Scanner s = new Scanner(new FileReader(filePath));
        s.useDelimiter(";");
        while (s.hasNextLine()) {
            conn.prepareStatement(s.nextLine()).execute();
        }

        conn.close();
    }

    @BeforeClass
    public static void databaseTests_createSchema() throws SQLException, IOException {
        String url = System.getenv(DATABASE_CONNECTION_ENV);
        assertNotNull(url);
        dataSource.setUrl(url);
        executeFile("src/test/sql/createSchema.sql");
        executeFile("src/test/sql/addData.sql");
    }

    @Test
    public void databaseTests_insert() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertEquals(1, conn.prepareStatement("INSERT INTO courses(name) VALUES ('LEIM');").executeUpdate());
        conn.close();
    }

    @Test
    public void databaseTests_delete() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertEquals(1, conn.prepareStatement("DELETE FROM students WHERE name LIKE 'A%'").executeUpdate());
        conn.close();
    }

    @Test
    public void databaseTests_select() throws SQLException {
        Connection conn = dataSource.getConnection();
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
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE students SET name = ? WHERE number = ?");
        stmt.setString(1, "Joana");
        stmt.setInt(2, 12346);
        assertEquals(1, stmt.executeUpdate());

        PreparedStatement stmtCheck = conn.prepareStatement("SELECT name FROM students WHERE number = 12346");
        ResultSet rst = stmtCheck.executeQuery();
        assertTrue(rst.next());
        assertEquals("Joana", rst.getString(1));
    }
}
