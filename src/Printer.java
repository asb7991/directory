import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class Printer {
    private Printer() {}

    public static void printAllCities(SortCityType type) throws SQLException {
        String query = "";
        switch (type) {
            case NOT_SORT:
                query = "SELECT * FROM city";
                break;
            case BY_NAME:
                query = "SELECT * FROM city ORDER BY UPPER(name)";
                break;
            case BY_DISTINCT_AND_NAME:
                query = "SELECT * FROM city ORDER BY district ASC, name ASC";
                break;
        }
        Statement statement = DataBase.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println("City{name='" + resultSet.getString("name") + "', region='" + resultSet.getString("region") +
                    "', district='" + resultSet.getString("district") + "', population=" + resultSet.getString("population") +
                    ", foundation='" + resultSet.getString("foundation") + "'}");
        }
        statement.close();
    }

    public static void printTheBiggestCity() throws SQLException {
        String query = "SELECT id, population FROM city";
        Statement statement = DataBase.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        Map<Integer, Integer> mapIdToPopulation = new HashMap<>();
        while (resultSet.next()) {
            mapIdToPopulation.put(resultSet.getInt("id"), resultSet.getInt("population"));
        }

        Map.Entry<Integer, Integer> max = Collections.max(mapIdToPopulation.entrySet(), Map.Entry.comparingByValue());
        System.out.println("[" + max.getKey() + "] = " + max.getValue());
        statement.close();
    }

    public static void printNumberOfCitiesByRegion() throws SQLException {
        String query = "SELECT region, COUNT(region) as count_of_city FROM city GROUP BY region";
        Statement statement = DataBase.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("region") + " - " + resultSet.getString("count_of_city"));
        }
        statement.close();
    }
}
