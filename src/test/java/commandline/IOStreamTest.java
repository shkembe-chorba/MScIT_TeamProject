package commandline;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class IOStreamTest {

    // Setup I/O for each test
    // ------------------------

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    protected void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    protected void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    // Helper to provide mock input
    protected void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    protected String getOut() {
        return outContent.toString();
    }

    protected String getErr() {
        return errContent.toString();
    }
}
