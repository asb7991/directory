import java.sql.*;

public class DataBase {

    private static DataBase instance;

    public Connection getConnection() {
        return connection;
    }

    private final Connection connection;

    private DataBase() throws SQLException {
        String url = "jdbc:h2:/Users/a19595494/IdeaProjects/directory/db/directoryDB;MV_STORE=false";
        String user = "user";
        String password = "user";
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(url, user, password);
    }

    public static DataBase getInstance() throws SQLException {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }
}
