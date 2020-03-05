package pt.isel.ls.db;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {

    private static Database getDatabase() {
        return new Database(System.getenv("POSTGRES_HOST"),
                System.getenv("POSTGRES_PORT"),
                System.getenv("POSTGRES_USERNAME"),
                System.getenv("POSTGRES_PASSWORD"),
                System.getenv("POSTGRES_DATABASE"));
    }

    private static void executeFile(String filePath) throws SQLException, IOException {
        Connection conn = getDatabase().getNewConnection();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            conn.prepareStatement(line).execute();
        }

        conn.close();
    }

    @BeforeClass
    public static void databaseTests_createSchema() throws IOException, SQLException {
        executeFile("test/sql/createSchema.sql");
    }

    @Test
    public void databaseTests_insert() throws SQLException, IOException {
        executeFile("test/sql/addData.sql");
    }

}
