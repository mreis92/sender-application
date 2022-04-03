package no.mnemonic.senderapplication;

import no.mnemonic.senderapplication.domain.Message;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class SocketClientTest {
    @SpyBean
    private SocketClient socketClient;

    @Mock
    private PrintWriter out;

    @Mock
    private BufferedReader in;

    @Test
    void testNoSocketConnection() throws IOException {
        assertThrows(IOException.class, () -> socketClient.startConnection(6666));

        verify(socketClient, times(3))
                .startConnection(6666);
    }

    @Test
    void testSendMessageNoSocketConnection() {
        var status = socketClient.sendMessage(new Message("test", "1", "test"));

        assertThat(status).isFalse();
    }
}
