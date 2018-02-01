package juja.sqlcmd;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String ADMIN_DB_NAME = "postgres";
    private static final String DB_ADMIN_LOGIN = "postgres";
    private static final String DB_ADMIN_PASSWORD = "postgres";
    private static final String DB_USER_LOGIN = "sqlcmd";
    private static final String DB_USER_PASSWORD = "sqlcmd";
    private static final String TEST_DB_NAME = "sqlcmd_test";


    private static Connection connection;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeClass
    public static void setConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_CONNECTION_URL + ADMIN_DB_NAME, DB_ADMIN_LOGIN, DB_ADMIN_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME);
        executeSqlQuery("CREATE DATABASE " + TEST_DB_NAME + " OWNER =" + DB_USER_PASSWORD);
        connection.close();
        connection = DriverManager.getConnection(DB_CONNECTION_URL + TEST_DB_NAME, DB_ADMIN_LOGIN, DB_ADMIN_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + DB_USER_LOGIN);
        executeSqlQuery("CREATE DATABASE " + DB_USER_LOGIN + " OWNER " + DB_USER_PASSWORD);
        connection.close();
    }

    @Before
    public void setUpStreams() {
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void out() throws SQLException, ClassNotFoundException {
        new Application().simpleSQL();
        String expected = "db is empty" + LINE_SEPARATOR +
                "user" + LINE_SEPARATOR + LINE_SEPARATOR +
                " 1 | user1 | password1" + LINE_SEPARATOR +
                " 2 | user2 | password2" + LINE_SEPARATOR +
                " 3 | user3 | password3" + LINE_SEPARATOR +
                LINE_SEPARATOR +
                " 1 | user1 | password1" + LINE_SEPARATOR +
                " 3 | user3 | password3" + LINE_SEPARATOR +
                " 2 | userсhange1 | password2" + LINE_SEPARATOR +
                LINE_SEPARATOR +
                " 1 | user1 | password1" + LINE_SEPARATOR +
                " 2 | userсhange1 | password2" + LINE_SEPARATOR +
                LINE_SEPARATOR;
        assertEquals(expected, outContent.toString());
    }

    private static void executeSqlQuery(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
        }
    }
}
