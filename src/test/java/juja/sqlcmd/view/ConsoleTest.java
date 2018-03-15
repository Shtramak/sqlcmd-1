package juja.sqlcmd.view;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private ByteArrayOutputStream outputStream;
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
        String message = "Привет world!";
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
        String message = "Привет world!";
        setInputStreamMessage(message);
        assertEquals(message, view.read());
    }

    @Test
    public void readWhenEmptyLineReturnsEmptyStringWithLineSeparator() {
        String message = "";
        setInputStreamMessage(message);
        assertEquals(message, view.read());
    }

    private void setInputStreamMessage(String message) {
        message = message + LINE_SEPARATOR;
        InputStream inputStream = new ByteArrayInputStream(message.getBytes());
        view = new Console(inputStream, null);
        System.setIn(inputStream);
    }
}