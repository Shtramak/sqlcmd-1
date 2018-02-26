package juja.sqlcmd;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JdbcDatabaseManagerTest extends AbstractDatabaseManagerTest {
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String ADMIN_DB_NAME = "postgres";
    private static final String DB_ADMIN_LOGIN = "postgres";
    private static final String DB_ADMIN_PASSWORD = "postgres";

    private static Connection connection;

    @BeforeClass
    public static void setConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_CONNECTION_URL + ADMIN_DB_NAME, DB_ADMIN_LOGIN, DB_ADMIN_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME);
        executeSqlQuery("CREATE DATABASE " + TEST_DB_NAME + " OWNER =" + DB_USER_LOGIN);
        connection.close();
        connection = DriverManager.getConnection(DB_CONNECTION_URL + TEST_DB_NAME, DB_ADMIN_LOGIN, DB_ADMIN_PASSWORD);
        executeSqlQuery("ALTER SCHEMA public OWNER TO " + DB_USER_LOGIN);
        connection.close();
        connection = DriverManager.getConnection(DB_CONNECTION_URL + TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
        connection = DriverManager.getConnection(DB_CONNECTION_URL + ADMIN_DB_NAME, DB_ADMIN_LOGIN, DB_ADMIN_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME);
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        super.init(new JdbcDatabaseManager());
    }

    @After
    public void closeDbManagerConnection() throws SQLException {
        databaseManager.close();
    }

    @Test
    public void connectWhenValidAllParametersReturnsTrue() {
        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidDatabaseNameReturnsFalse() {
        assertFalse(databaseManager.connect("Notsqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidUserNameReturnsFalse() {
        assertFalse(databaseManager.connect("sqlcmd", "Notsqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidUserPasswordReturnsFalse() {
        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "Notsqlcmd"));
    }

    @Override
    void createTestTableWithIdAndName(String tableName) throws SQLException {
        String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s(" +
                "id INTEGER," +
                "name VARCHAR(128)" +
                ")", tableName);
        executeSqlQuery(sqlQuery);
    }

    @Override
    void insertData(String tableName, DataSet row) throws SQLException {
        String valuesInLine = "";
        for (String value : row.values()) {
            valuesInLine += String.format("'%s',", value);
        }
        valuesInLine = valuesInLine.substring(0, valuesInLine.length() - 1);
        String sqlQuery = String.format("INSERT INTO %s VALUES(%s)", tableName, valuesInLine);
        executeSqlQuery(sqlQuery);
    }

    @Override
    void dropTables(String... tableNames) throws SQLException {
        String tableNamesAsString = String.join(",", tableNames);
        executeSqlQuery("DROP TABLE IF EXISTS " + tableNamesAsString + " CASCADE");
    }

    private static void executeSqlQuery(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
        }
    }
}