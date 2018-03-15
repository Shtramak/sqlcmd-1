package juja.sqlcmd.view;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private View view;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        view = new Console();
    }

    @Test
    public void writeWhenRegularString() {
        String message = "Hello world!";
        view.write(message);
        String actual = outputStream.toString();
        assertEquals(message, actual);
    }

    @Test
    public void writeWhenEmptyString() {
        String message = "";
        view.write(message);
        String actual = outputStream.toString();
        assertEquals(message, actual);
    }

    @Test
    public void readWhenRegularLineReturnsLineWithLineSeparator() {
        String message = "Hello world!";
        setInputStreamMessage(message);
        String expected = message + System.lineSeparator();
        assertEquals(expected, view.read());
    }

    @Test
    public void readWhenEmptyLineReturnsEmptyStringWithLineSeparator() {
        String message = "";
        setInputStreamMessage(message);
        String expected = message + System.lineSeparator();
        assertEquals(expected, view.read());
    }

    private void setInputStreamMessage(String message) {
        message = message + System.lineSeparator();
        inputStream = new ByteArrayInputStream(message.getBytes());
        view = new Console(inputStream, null);
        System.setIn(inputStream);
    }
}
