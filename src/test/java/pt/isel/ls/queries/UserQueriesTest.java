package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.model.User;
import pt.isel.ls.sql.queries.UserQueries;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.DatabaseTest.executeFile;

public class UserQueriesTest {

    private static final DataSource dSource = TestDatasource.getDataSource();


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

    @Test
    public void testGetUserByNameAndEmail() throws Throwable {
        Connection conn = dSource.getConnection();
        UserQueries query = new UserQueries(conn);

        String name = "UserTest";
        String mail = "UserTest@Teste.com";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO \"user\" (email, name) VALUES (?, ?);");
        stmt.setString(1,mail);
        stmt.setString(2,name);
        stmt.execute();

        User test = query.getUser(name,mail);

        Assert.assertNotNull(test);
        Assert.assertEquals(name,test.getName());
        Assert.assertEquals(mail, test.getEmail());
    }

    @Test
    public void testGetUserById() throws Throwable {
        Connection conn = dSource.getConnection();

        String name = "UIDTest";
        String mail = "UIDTest@Teste.com";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO \"user\" (email, name) VALUES (?, ?);");
        stmt.setString(1,mail);
        stmt.setString(2,name);
        stmt.execute();

        PreparedStatement qstmt = conn.prepareStatement("SELECT uid FROM \"user\" WHERE email = ? AND name = ?;");
        qstmt.setString(1,mail);
        qstmt.setString(2,name);
        ResultSet res = qstmt.executeQuery();
        res.next();

        UserQueries query = new UserQueries(conn);
        User test = query.getUser(res.getInt(1));

        Assert.assertNotNull(test);
        Assert.assertEquals(name,test.getName());
        Assert.assertEquals(mail,test.getEmail());
    }
    //TODO: Add description tests
}
