package ru.geekbrains.homework9;

/*
    Создать класс кота, с полями имя, цвет, что-нибудь еще.
    Создать в базе таблицу с котами.
    ***Сделать класс для работы с котами в бд: запись кота в БД, чтение кота из базы, изменение...
 */

import java.sql.*;

public class Database {
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement ps;

    public static void main(String[] args) {

        Cats[] cats = new Cats[] {
                new Cats("Барсик", "Белый", 3),
                new Cats("Рыжик", "Рыжий", 4),
                new Cats("Тиша", "Пепельная", 2),
                new Cats("Муся", "Трехцветная", 5)
        };

        try {
            connect();
            dropCreate();
            prepareStatement();

            for (Cats cat : cats) {
                ps.setString(1, cat.getName());
                ps.setString(2, cat.getColor());
                ps.setInt(3, cat.getAge());
                ps.executeUpdate();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            disconnect();
        }
    }


    private static void dropCreate() throws SQLException {
        statement.execute("drop table if exists cats;");
        statement.execute("create table if not exists cats (id integer primary key autoincrement," +
                " name text, color text, age integer);");
    }

    private static void prepareStatement() throws SQLException {
        ps = connection.prepareStatement("insert into cats (name, color, age) values (?, ?, ?);");
    }

    private static void disconnect() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:cats.db");
        statement = connection.createStatement();
    }
}
