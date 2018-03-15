package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class InMemoryDatabaseManagerTest extends AbstractDatabaseManagerTest {
    private InMemoryDatabaseManager inMemoryDatabaseManager;

    @Before
    public void setUp() throws SQLException {
        super.init(new InMemoryDatabaseManager());
    }

    @Test
    public void connectAlwaysReturnsTrue() {
        assertTrue(databaseManager.connect(null, null, null));
    }

    @Override
    void createTestTableWithIdAndName(String tableName) {
        inMemoryDatabaseManager = (InMemoryDatabaseManager) databaseManager;
        inMemoryDatabaseManager.createTable(tableName, 2);
    }

    @Override
    void insertData(String tableName, DataSet row) {
        inMemoryDatabaseManager = (InMemoryDatabaseManager) databaseManager;
        inMemoryDatabaseManager.insert(tableName, row);
    }

    @Override
    void dropTables(String... tableNames) {
        //NOP
    }
}