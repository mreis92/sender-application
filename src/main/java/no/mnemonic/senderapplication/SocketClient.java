package no.mnemonic.senderapplication;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import no.mnemonic.senderapplication.domain.Message;
import no.mnemonic.senderapplication.domain.StatusMessage;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
@Service
public class SocketClient {
    private final Gson gson;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Retryable(
            include = {IOException.class},
            backoff = @Backoff(delay = 1000))
    public void startConnection(int port) throws IOException {
        try {
            clientSocket = new Socket("127.0.0.1", port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.printf("Could not connect to socket on port %s: %s%n", port, e.getMessage());
            throw e;
        }
    }

    public boolean sendMessage(Message message) {
        try {
            out.println(gson.toJson(message));

            var statusMessage = gson.fromJson(in.readLine(), StatusMessage.class);

            return statusMessage.isOk();
        } catch (IOException e) {
            System.err.printf("Could not send message to socket: %s%n", e.getMessage());
            return false;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.printf("Could not end connection to socket: %s%n", e.getMessage());
        }
    }
}
