package no.mnemonic.senderapplication;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import no.mnemonic.senderapplication.domain.Message;
import no.mnemonic.senderapplication.domain.StatusMessage;
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

    public void startConnection(int port) throws IOException {
        clientSocket = new Socket("127.0.0.1", port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public boolean sendMessage(Message message) {
        try {
            out.println(gson.toJson(message));

            var statusMessage = gson.fromJson(in.readLine(), StatusMessage.class);
            
            return statusMessage.isOk();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
