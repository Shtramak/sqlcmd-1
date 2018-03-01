package juja.sqlcmd;

import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

abstract class AbstractDatabaseManagerTest {
    static final String DB_USER_LOGIN = "sqlcmd";
    static final String DB_USER_PASSWORD = "sqlcmd";
    static final String TEST_DB_NAME = "sqlcmd_test";
    static final String TEST_TABLE_NAME = "test_table";

    DatabaseManager databaseManager;

    public void init(DatabaseManager databaseManager) throws SQLException {
        this.databaseManager = databaseManager;
    }

    @After
    public void tearDown() throws SQLException {
        databaseManager.close();
    }

    @Test
    public void getTableNamesWhenNoTablesInDbReturnsEmptyArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        String[] expected = new String[]{};
        assertArrayEquals(expected, databaseManager.getTableNames());
    }

    @Test
    public void getTableNamesWhenTwoTablesInDbReturnsTableNamesArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        createTestTableWithIdAndName("table1");
        createTestTableWithIdAndName("table2");
        String[] expected = new String[]{"table1", "table2"};
        assertArrayEquals(expected, databaseManager.getTableNames());
        dropTables("table1,table2");
    }

    @Test
    public void getTableDataWhenEmptyTableReturnsEmptyArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        DataSet[] expected = new DataSet[]{};
        assertArrayEquals(expected, databaseManager.getTableData(TEST_TABLE_NAME));
        dropTables(TEST_TABLE_NAME);
    }

    @Test
    public void getTableDataWhenTableNotExistsReturnsEmptyArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        DataSet[] expected = new DataSet[]{};
        assertArrayEquals(expected, databaseManager.getTableData("WrongTableName"));
    }

    @Test
    public void getTableDataWhenValidDataReturnsTableDataArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        insertData(TEST_TABLE_NAME, row1);
        DataSet row2 = new DataSet(2);
        row2.insertValue(0, "2");
        row2.insertValue(1, "name2");
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        insertData(TEST_TABLE_NAME, row2);
        DataSet[] expected = new DataSet[]{row1, row2};
        DataSet[] actual = databaseManager.getTableData(TEST_TABLE_NAME);
        assertThat(actual, arrayContainingInAnyOrder(expected));
        dropTables(TEST_TABLE_NAME);
    }

    @Test
    public void insertWhenValidDataReturnsTrue() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        DataSet tableRow = new DataSet(2);
        tableRow.insertValue(0, "1");
        tableRow.insertValue(1, "name1");
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        assertTrue(databaseManager.insert(TEST_TABLE_NAME, tableRow));
        dropTables(TEST_TABLE_NAME);
    }

    @Test
    public void insertWhenTableNotExistsReturnsFalse() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        DataSet tableRow = new DataSet(2);
        tableRow.insertValue(0, "1");
        tableRow.insertValue(1, "name1");
        assertFalse(databaseManager.insert("WrongTableName", tableRow));
    }

    @Test
    public void insertWhenInvalidNumberOfParametersReturnsFalse() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        DataSet tableRow = new DataSet(3);
        tableRow.insertValue(0, "1");
        tableRow.insertValue(1, "name1");
        tableRow.insertValue(2, "name2");
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        assertFalse(databaseManager.insert(TEST_TABLE_NAME, tableRow));
        dropTables(TEST_TABLE_NAME);
    }

    @Test
    public void deleteWhenTableNotExistsReturnsFalse() {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        assertFalse(databaseManager.delete("WrongTableName", 1));
    }

    @Test
    public void deleteWhenValidDataReturnsTrue() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        insertData(TEST_TABLE_NAME, row1);
        DataSet row2 = new DataSet(2);
        row2.insertValue(0, "2");
        row2.insertValue(1, "name2");
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        insertData(TEST_TABLE_NAME, row2);
        assertTrue(databaseManager.delete(TEST_TABLE_NAME, 1));
        dropTables(TEST_TABLE_NAME);
    }

    @Test
    public void deleteWhenNotExistedIdReturnsFalse() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER_LOGIN, DB_USER_PASSWORD);
        createTestTableWithIdAndName(TEST_TABLE_NAME);
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        insertData(TEST_TABLE_NAME, row1);
        assertFalse(databaseManager.delete(TEST_TABLE_NAME, 2));
        dropTables(TEST_TABLE_NAME);
    }

    abstract void createTestTableWithIdAndName(String tableName) throws SQLException;

    abstract void insertData(String testTableName, DataSet row) throws SQLException;

    abstract void dropTables(String... tableNames) throws SQLException;
}
