package exercises;

import helpers.DBConnector;

import java.sql.*;

public class Exercise {
    public static DBConnector conn;
    public static Connection connection;
    public static Statement statement;
    static{
        conn = new DBConnector();
        connection = conn.connection;
        statement = conn.statement;
    }
    public static void main(String[] args) {

        try {
            cleanTable();
            insertBatchQuery();
            insertUncommittedQuery();
            selectQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    private static void cleanTable() throws SQLException {
        statement.execute("truncate students");
    }

    private static void insertBatchQuery() throws SQLException {
        //disable autocommit, and then we use it on our own
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into students(name, score) values(?,?)");
        for (int i = 0; i < 100; i++) {
            preparedStatement.setString(1, "john"+i);
            preparedStatement.setInt(2, (int) (99 * Math.random()));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.commit();
    }

    private static void insertUncommittedQuery() throws SQLException {
        //disable autocommit, and then we use it on our own
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into students(name, score) values(?,?)");
        try {

            for (int i = 0; i < 100; i++) {
                preparedStatement.setString(1, "john" + i);
                preparedStatement.setInt(2, (int) (99 * Math.random()));
                preparedStatement.execute();
                if (i > 50) {
                    throw new RuntimeException();
                }
            }
            connection.commit();
        } catch (Exception E) {
            connection.rollback();
        }

    }

    private static void selectQuery() throws SQLException {
        String sqlQuery = "select name, score from students where score > 80 order by score desc";
        //ResultSet implement AutoCloseable, that mean it close automatically, if we use try-with-resource
        try(ResultSet rs = statement.executeQuery(sqlQuery)) {
            while(rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                System.out.println(name + " " + score);
            }
        }
    }
}