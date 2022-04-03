package no.mnemonic.senderapplication.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Message {
    private String senderId;
    private String receiverId;
    private String content;
}
