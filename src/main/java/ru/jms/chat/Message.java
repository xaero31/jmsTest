package ru.jms.chat;

import lombok.Data;

import java.util.Date;

/**
 * @author Nikita Ermakov
 */
@Data
public class Message {

    private String text;

    private String sender;

    private Date date;

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.date = new Date();
    }
}
