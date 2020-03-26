package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.sql.queries.UserQueries;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserQueriesTest {

    private static final DataSource dSource = TestDatasource.getDataSource();

    private static void executeFile(String filePath) throws SQLException, IOException {
        Connection conn = dSource.getConnection();
        Scanner s = new Scanner(new FileReader(filePath));
        s.useDelimiter(";");
        while (s.hasNextLine()) {
            conn.prepareStatement(s.nextLine()).execute();
        }

        conn.close();
    }

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");
    }

    @Test
    public void testCreateNewUser() throws Throwable {
        Connection conn = dSource.getConnection();
        UserQueries query = new UserQueries(conn);
        final String testUser = "TestUser";
        final String mail = "Teste@Teste.com";
        query.createNewUser(testUser, mail);
        PreparedStatement stmt = conn.prepareStatement("SELECT name , email "
                + "FROM \"user\" WHERE name = ? AND email = ?");
        stmt.setString(1, testUser);
        stmt.setString(2, mail);
        ResultSet res = stmt.executeQuery();
        if (res.next()) {
            Assert.assertEquals(testUser, res.getString(1));
            Assert.assertEquals(mail, res.getString(2));
        } else {
            Assert.fail("ResultSet is Empty");
        }
        conn.close();
    }

    public void testGetUser() throws SQLException {
        Connection conn = dSource.getConnection();
    }
}
