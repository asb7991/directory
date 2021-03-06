import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBase {

    private static DataBase instance;

    private final Connection connection;
    private final Properties prop;

    public Connection getConnection() {
        return connection;
    }

    public Properties getProp() {
        return prop;
    }

    private DataBase() throws SQLException {
        prop = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("resources/config.properties");
            prop.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
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
