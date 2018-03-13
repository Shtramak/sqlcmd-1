package juja.sqlcmd.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console implements View {
    private InputStream inputStream;
    private PrintStream printStream;

    public Console(PrintStream printStream, InputStream inputStream) {
        this.printStream = printStream;
        this.inputStream = inputStream;
    }

    @Override
    public void write(String message) {
        try {
            printStream.write(message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Something goes wrong... Reason:" + e.getMessage());
        }
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(inputStream);
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}