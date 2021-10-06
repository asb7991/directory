import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        String fileName = Singleton.getInstance().getProp().getProperty("filepath");
        Path path = Paths.get(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connection connection = Singleton.getInstance().getConnection();
        Statement statement = connection.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS city (id   INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "                   name VARCHAR(20) NOT NULL,\n" +
                "                   region VARCHAR(20),\n" +
                "                   district VARCHAR(20) NOT NULL,\n" +
                "                   population NUMBER,\n" +
                "                   foundation NUMBER\n" +
                ")";
        statement.execute(query);

        scanner.useDelimiter(System.getProperty("line.separator"));
        while (scanner.hasNext()) {
            String[] result = scanner.next().split(";");
            query = "SELECT id FROM city WHERE id = " + result[0];
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                query = "INSERT INTO city (id, name, region, district, population, foundation) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                for (int index = 0; index < result.length; index++) {
                    preparedStatement.setObject(index + 1, result[index]);
                }
                preparedStatement.execute();
                preparedStatement.close();
            }
        }
        scanner.close();
        statement.close();

        System.out.println("Меню:");
        System.out.println("1) Список городов");
        System.out.println("2) Отсортированный список городов по наименованию");
        System.out.println("3) Отсортированный список городов по округу и наименованию");
        System.out.println("4) Индекс города с наибольшим населением");
        System.out.println("5) Количество городов в разрезе регионов");
        System.out.println("0) Выход");

        scanner = new Scanner(System.in);
        int answer = 0;
        do {
            System.out.print("directory: ");
            answer = scanner.nextInt();
            switch (answer) {
                case 1:
                    Main.printAllCities(SortCityType.NOT_SORT);
                    break;
                case 2:
                    Main.printAllCities(SortCityType.BY_NAME);
                    break;
                case 3:
                    Main.printAllCities(SortCityType.BY_DISTINCT_AND_NAME);
                    break;
                case 4:
                    Main.printTheBiggestCity();
                    break;
                case 5:
                    Main.printNumberOfCitiesByRegion();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Неверная команда!");
                    break;
            }
        } while (answer != 0);

    }

    private static void printAllCities(SortCityType type) throws SQLException {
        String query = "";
        switch (type) {
            case NOT_SORT:
                query = "SELECT * FROM city";
                break;
            case BY_NAME:
                query = "SELECT * FROM city ORDER BY name";
                break;
            case BY_DISTINCT_AND_NAME:
                query = "SELECT * FROM city ORDER BY district ASC, name ASC";
                break;
        }
        Statement statement = Singleton.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println("City{name='" + resultSet.getString("name") + "', region='" + resultSet.getString("region") +
                    "', district='" + resultSet.getString("district") + "', population=" + resultSet.getString("population") +
                    ", foundation='" + resultSet.getString("foundation") + "'}");
        }
        statement.close();
    }

    private static void printTheBiggestCity() throws SQLException {
        String query = "SELECT population FROM city";
        Statement statement = Singleton.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<Integer> array = new ArrayList<>();
        while (resultSet.next()) {
            array.add(resultSet.getInt("population"));
        }
        int maxIndex = 0, maxElement = array.get(0);
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > maxElement) {
                maxElement = array.get(i);
                maxIndex = i;
            }
        }
        System.out.println("[" + (maxIndex + 1) + "] = " + maxElement);
        statement.close();
    }

    private static void printNumberOfCitiesByRegion() throws SQLException {
        String query = "SELECT region, COUNT(region) as count_of_city FROM city GROUP BY region";
        Statement statement = Singleton.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("region") + " - " + resultSet.getString("count_of_city"));
        }
        statement.close();
    }

}

