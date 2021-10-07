import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        String fileName = DataBase.getInstance().getProp().getProperty("filepath");
        Path path = Paths.get(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connection connection = DataBase.getInstance().getConnection();
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
            try {
                answer = scanner.nextInt();
            } catch (InputMismatchException e) {
                answer = 0;
            }
            switch (answer) {
                case 1:
                    Printer.printAllCities(SortCityType.NOT_SORT);
                    break;
                case 2:
                    Printer.printAllCities(SortCityType.BY_NAME);
                    break;
                case 3:
                    Printer.printAllCities(SortCityType.BY_DISTINCT_AND_NAME);
                    break;
                case 4:
                    Printer.printTheBiggestCity();
                    break;
                case 5:
                    Printer.printNumberOfCitiesByRegion();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Неверная команда!");
                    break;
            }
        } while (answer != 0);

    }

}

