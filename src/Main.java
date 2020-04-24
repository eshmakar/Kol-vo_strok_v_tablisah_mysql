import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson?useSSL=false&serverTimezone=UTC";
        String userName = "root";
        String password = "root";
        Class.forName("com.mysql.cj.jdbc.Driver"); //новый драйвер, сj, без этого не работает

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            CallableStatement callableStatement = null;
            try {
                callableStatement = conn.prepareCall("{call tablesCount()}"); //создаем подключение к процедуру
                boolean hasResults = callableStatement.execute(); //если выполнение прошло успешно - true!
                ResultSet resultSet = null;

                try {
                    while (hasResults) { //если true
                        resultSet = callableStatement.getResultSet();
                        while (resultSet.next()) {
                            System.out.println("Кол-во записей в таблице: " + resultSet.getInt(1));
                        }
                        hasResults = callableStatement.getMoreResults();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (resultSet != null)
                        resultSet.close();
                    else System.err.println("Ошибка чтения с БД");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                callableStatement.close();
            }
        }
    }
}

//Процедура
/*create procedure tablesCount ()
begin
    select count(*) from books;
    select count(*) from books2;
end;*/