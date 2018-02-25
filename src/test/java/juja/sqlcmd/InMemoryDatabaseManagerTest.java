package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class InMemoryDatabaseManagerTest {
    private InMemoryDatabaseManager databaseManager;

    @Before
    public void setUp() {
        databaseManager = new InMemoryDatabaseManager();
    }

    @Test
    public void connectAlwaysReturnsTrue() {
        assertTrue(databaseManager.connect(null, null, null));
    }

    @Test
    public void getTableNamesWhenNoTablesInDbReturnsEmptyArray() throws SQLException {
        String[] expected = new String[]{};
        assertArrayEquals(expected, databaseManager.getTableNames());
    }

    @Test
    public void getTableNamesWhenTwoTablesInDbReturnsTableNamesArray() throws SQLException {
        databaseManager.createTable("table1", 0);
        databaseManager.createTable("table2", 0);
        String[] expected = new String[]{"table1", "table2"};
        assertArrayEquals(expected, databaseManager.getTableNames());
    }

    @Test
    public void getTableDataWhenEmptyTableReturnsEmptyArray() throws SQLException {
        databaseManager.createTable("table", 0);
        DataSet[] expected = new DataSet[]{};
        assertArrayEquals(expected, databaseManager.getTableData("table"));
    }

    @Test
    public void getTableDataWhenTableNotExistsReturnsEmptyArray() throws SQLException {
        DataSet[] expected = new DataSet[]{};
        assertArrayEquals(expected, databaseManager.getTableData("WrongTableName"));
    }

    @Test
    public void getTableDataWhenValidDataReturnsTableDataArray() throws SQLException {
        databaseManager.createTable("table", 2);
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        databaseManager.insert("table",row1);
        DataSet row2 = new DataSet(2);
        row2.insertValue(0, "2");
        row2.insertValue(1, "name2");
        databaseManager.insert("table",row2);
        DataSet[] expected = new DataSet[]{row1, row2};
        DataSet[] actual = databaseManager.getTableData("table");
        assertThat(actual, arrayContainingInAnyOrder(expected));
    }

    @Test
    public void insertWhenValidDataReturnsTrue() throws SQLException {
        DataSet tableRow = new DataSet(2);
        tableRow.insertValue(0, "1");
        tableRow.insertValue(1, "name1");
        databaseManager.createTable("table",2);
        assertTrue(databaseManager.insert("table", tableRow));
    }

    @Test
    public void insertWhenTableNotExistsReturnsFalse() throws SQLException {
        DataSet tableRow = new DataSet(2);
        tableRow.insertValue(0, "1");
        tableRow.insertValue(1, "name1");
        assertFalse(databaseManager.insert("WrongTableName", tableRow));
    }

    @Test
    public void insertWhenInvalidNumberOfParametersReturnsFalse() throws SQLException {
        DataSet tableRow = new DataSet(3);
        tableRow.insertValue(0, "1");
        tableRow.insertValue(1, "name1");
        tableRow.insertValue(2, "name2");
        databaseManager.createTable("table",2);
        assertFalse(databaseManager.insert("table", tableRow));
    }

    @Test
    public void deleteWhenTableNotExistsReturnsFalse() {
        assertFalse(databaseManager.delete("WrongTableName", 1));
    }

    @Test
    public void deleteWhenValidDataReturnsTrue() throws SQLException {
        databaseManager.createTable("table", 2);
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        databaseManager.insert("table",row1);
        DataSet row2 = new DataSet(2);
        row2.insertValue(0, "2");
        row2.insertValue(1, "name2");
        databaseManager.insert("table",row2);
        assertTrue(databaseManager.delete("table", 1));
    }

    @Test
    public void deleteWhenNotExistedIdReturnsFalse() throws SQLException {
        databaseManager.createTable("table", 2);
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        databaseManager.insert("table",row1);
        assertFalse(databaseManager.delete("table", 2));
    }
}