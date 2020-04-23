package pt.isel.ls;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class DatasourceUtils {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_TEST_DATABASE_URL";
    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        dataSource.setUrl(System.getenv(DATABASE_CONNECTION_ENV));
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void executeFile(String filePath) {
        try (Connection conn = dataSource.getConnection()) {
            InputStream is = DatasourceUtils.class.getClassLoader()
                    .getResourceAsStream("sql/" + filePath);

            if (is == null) {
                throw new IOException("Resource does not exist!");
            }

            Scanner s = new Scanner(is);
            s.useDelimiter(";");
            while (s.hasNextLine()) {
                conn.prepareStatement(s.nextLine()).execute();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
