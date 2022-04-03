package no.mnemonic.senderapplication;

import no.mnemonic.senderapplication.domain.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class InputListenerTest {
    @Autowired
    private InputListener inputListener;

    @MockBean
    private SocketClient client;

    private final String SENDER_NAME = "test";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testExitMessage() throws Exception {
        int statusCode = catchSystemExit(() -> inputListener.processUserInput(SENDER_NAME, "exit"));

        assertThat(statusCode).isEqualTo(0);
    }

    @Test
    void testMessageToAllSuccess() {
        inputListener.processUserInput(SENDER_NAME, "@all: this is a test message");

        var message = new Message(SENDER_NAME, "all", "this is a test message");

        verify(client, times(1))
                .sendMessage(message);
    }

    @Test
    void testMessageToReceiverSuccess() {
        inputListener.processUserInput(SENDER_NAME, "@receiver1: this is a test message");

        var message = new Message(SENDER_NAME, "1", "this is a test message");

        verify(client, times(1))
                .sendMessage(message);
    }

    @Test
    void testMessageToReceiverDoesNotExist() {
        inputListener.processUserInput(SENDER_NAME, "@receiver2: this is a test message");

        var message = new Message(SENDER_NAME, "receiver2", "this is a test message");

        when(client.sendMessage(message)).thenReturn(false);

        assertThat(outContent.toString()).isEqualTo("No receiver found for @receiver2\n");
    }

    @Test
    void testInvalidMessage() {
        inputListener.processUserInput(SENDER_NAME, "this message will not be sent");

        assertThat(outContent.toString()).isEqualTo("invalid message\n");
        verify(client, times(0))
                .sendMessage(any(Message.class));
    }
}
