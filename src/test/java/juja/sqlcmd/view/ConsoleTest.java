package juja.sqlcmd.view;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    @Test
    public void writeWhenRegularString() {
        View view = new Console(System.out, null);
        String message = "Hello world!";
        view.write(message);
        String actual = outputStream.toString();
        assertEquals(message, actual);
    }

    @Test
    public void writeWhenEmptyString() {
        View view = new Console(System.out, null);
        String message = "";
        view.write(message);
        String actual = outputStream.toString();
        assertEquals(message, actual);
    }

    @Test
    public void readWhenRegularLineReturnsLine() {
        String message = "Hello world!";
        View view = new Console(null, new ByteArrayInputStream(message.getBytes()));
        assertEquals(message, view.read());
    }

    @Test
    public void readWhenEmptyLineReturnsEmptyString() {
        String message = "";
        View view = new Console(null, new ByteArrayInputStream(message.getBytes()));
        assertEquals(message, view.read());
    }
}
