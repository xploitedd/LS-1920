package pt.isel.ls;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class DatasourceUtils {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_TEST_DATABASE_URL";
    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    public static DataSource getDataSource() {
        dataSource.setUrl(System.getenv(DATABASE_CONNECTION_ENV));
        return dataSource;
    }

    public static void executeFile(DataSource ds, String filePath) throws SQLException, IOException {
        Connection conn = ds.getConnection();
        Scanner s = new Scanner(new FileReader(filePath));
        s.useDelimiter(";");
        while (s.hasNextLine()) {
            conn.prepareStatement(s.nextLine()).execute();
        }

        conn.close();
    }

}
