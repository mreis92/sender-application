package no.mnemonic.senderapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SenderApplication {

    public static void main(String[] args) throws IOException {
        var senderName = args[0];
        var port = Integer.parseInt(args[1]);

        var context = SpringApplication.run(SenderApplication.class, args);

        var socketClient = context.getBean(SocketClient.class);
        socketClient.startConnection(port);

        var inputListener = context.getBean(InputListener.class);
        inputListener.listenForUserInput(senderName);
    }
}
