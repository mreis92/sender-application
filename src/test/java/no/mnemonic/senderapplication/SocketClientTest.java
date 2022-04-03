package no.mnemonic.senderapplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class SocketClientTest {
    @SpyBean
    private SocketClient socketClient;

    private final int PORT = 6667;

    @Test
    void testNoSocketConnection() throws IOException {
        assertThrows(IOException.class, () -> socketClient.startConnection(PORT));

        verify(socketClient, times(3))
                .startConnection(PORT);
    }
}
