package ru.fizteh.fivt.students.egiby.miniorm;

import org.h2.jdbcx.JdbcConnectionPool;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by egiby on 17.12.15.
 */
public class DatabaseService<T> implements Closeable {
    private Class<T> workingClass;

    private String connectionName;
    private String username;
    private String password;

    private String tableName;
    private Field[] fields;
    private int primaryKeyId = -1;

    private JdbcConnectionPool databasePool;

    private void parseProperties(String properties) {
        Properties settings = new Properties();

        try (InputStream input = this.getClass().getResourceAsStream(properties)) {
            settings.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectionName = settings.getProperty("connection_name");
        username = settings.getProperty("username");
        password = settings.getProperty("password");
    }

    private void init() throws IllegalArgumentException {
        parseProperties("/h2.properties");
        if (!workingClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("table class without @Table annotation");
        }

        tableName = workingClass.getName();
        if (tableName.equals("")) {
            tableName = NamesHelper.convert(workingClass.getSimpleName());
        }

        if (!NamesHelper.isGood(tableName)) {
            throw new IllegalArgumentException("bad table name");
        }

        List<Field> allFields = new ArrayList<>();
        Set<Field> set = new HashSet<>();

        for (Field f: workingClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class)) {
                if (!NamesHelper.isGood(f.getName())) {
                    throw new IllegalArgumentException("bad column name");
                }

                allFields.add(f);
                set.add(f);

                if (f.isAnnotationPresent(PrimaryKey.class)) {
                    if (primaryKeyId != -1) {
                        throw new IllegalArgumentException("not single @PrimaryKey");
                    }

                    primaryKeyId = allFields.size() - 1;
                }
            } else if (f.isAnnotationPresent(PrimaryKey.class)) {
                throw new IllegalArgumentException("@PrimaryKey, but not @Column");
            }
        }

        if (set.size() != allFields.size()) {
            throw new IllegalArgumentException("There are two columns with one name");
        }

        fields = new Field[allFields.size()];
        allFields.toArray(fields);

        databasePool = JdbcConnectionPool.create(connectionName, username, password);
    }

    DatabaseService(Class<T> className) {
        workingClass = className;
        init();
    }

    private void executeSimpleRequest(String request) throws SQLException {
        try (Connection connection =  databasePool.getConnection()) {
            connection.createStatement().execute(request);
        }
    }

    public void createTable() throws SQLException {
        StringBuilder request = new StringBuilder();
        request.append("CREATE TABLE IF NOT EXISTS ");
        request.append(tableName + "(");
        for (int i = 0; i < fields.length; ++i) {
            request.append(fields[i].getName() + " " + NamesHelper.getSQLTypeName(fields[i].getType()));
            if (i == primaryKeyId) {
                request.append(" PRIMARY KEY ");
            }

            if (i != fields.length - 1) {
                request.append(", ");
            }
        }

        request.append(")");
        executeSimpleRequest(request.toString());
    }

    public void dropTable() throws SQLException {
        String request = "DROP TABLE IF EXISTS " + tableName;
        executeSimpleRequest(request.toString());
    }

    public void delete(T record) throws IllegalArgumentException, SQLException, IllegalAccessException {
        if (primaryKeyId == -1) {
            throw new IllegalArgumentException("There is no primary key in the table");
        }

        StringBuilder request = new StringBuilder();

        request.append("DELETE FROM " + tableName + " ").append("WHERE ").
                append(fields[primaryKeyId].getName()).append(" = ?");

        try (Connection connection =  databasePool.getConnection()) {
            PreparedStatement statement
                    = connection.prepareStatement(request.toString());
            statement.setObject(1, fields[primaryKeyId].get(record));
            statement.execute();
        }
    }

    public void insert(T record) throws SQLException, IllegalAccessException {
        StringBuilder request = new StringBuilder();

        request.append("INSERT INTO " + tableName + " VALUES(");
        for (int i = 0; i < fields.length; ++i) {
            request.append("?");
            if (i != fields.length - 1) {
                request.append(", ");
            }
        }
        request.append(")");

        try (Connection connection =  databasePool.getConnection()) {
            PreparedStatement statement
                    = connection.prepareStatement(request.toString());
            for (int i = 0; i < fields.length; ++i) {
                statement.setObject(i + 1, fields[i].get(record));
            }

            statement.execute();
        }
    }

    public void update(T record) throws IllegalArgumentException, SQLException, IllegalAccessException {
        if (primaryKeyId == -1) {
            throw new IllegalArgumentException("There is no primary key in the table");
        }

        StringBuilder request = new StringBuilder();

        request.append("UPDATE TABLE " + tableName).append(" SET ");
        for (int i = 0; i < fields.length; ++i) {
            if (i == primaryKeyId) {
                continue;
            }

            request.append(fields[i].getName() + " = ?");
            if (i != fields.length - 1) {
                request.append(", ");
            }
        }

        request.append(" WHERE ").append(fields[primaryKeyId].getName() + " = ?");

        try (Connection connection =  databasePool.getConnection()) {
            PreparedStatement statement
                    = connection.prepareStatement(request.toString());
            int current = 1;
            for (int i = 0; i < fields.length; ++i) {
                if (i != primaryKeyId) {
                    statement.setObject(current++, fields[i].get(record));
                }
            }

            statement.setObject(current, fields[primaryKeyId].get(record));
            statement.execute();
        }
    }

    /*<K> T queryById(K key) {

    }

    List<T> queryForAll() {

    }*/

    @Override
    public void close() {

    }
}
