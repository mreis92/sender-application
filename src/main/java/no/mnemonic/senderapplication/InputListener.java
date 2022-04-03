package no.mnemonic.senderapplication;

import lombok.RequiredArgsConstructor;
import no.mnemonic.senderapplication.domain.Message;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class InputListener {
    private final Pattern allPattern = Pattern.compile("^@all: (.*)$");
    private final Pattern targetPattern = Pattern.compile("^@receiver([1-9][0-9]*): (.*)$");

    private final SocketClient client;
    
    public void listenForUserInput(String senderName) {
        var in = new Scanner(System.in);
        String inputLine;

        while ((inputLine = in.nextLine()) != null) {
            processUserInput(senderName, inputLine.trim());
        }
    }

    protected void processUserInput(String senderName, String line) {
        if (line.equals("exit")) {
            client.stopConnection();
            System.exit(0);
            return;
        }

        var allMatcher = allPattern.matcher(line);
        if (allMatcher.matches()) {
            var message = new Message(senderName, "all", allMatcher.group(1));
            client.sendMessage(message);
            return;
        }

        var targetMatcher = targetPattern.matcher(line);
        if (targetMatcher.matches()) {
            var receiverId = targetMatcher.group(1);
            var message = new Message(senderName, receiverId, targetMatcher.group(2));

            if (!client.sendMessage(message)) {
                System.out.println("No receiver found for @receiver" + receiverId);
            }

            return;
        }

        System.out.println("invalid message");
    }
}
