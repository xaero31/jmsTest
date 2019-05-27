package ru.jms.chat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikita Ermakov
 */
@Singleton
public class MessagePrinter {

    private List<Message> messages;

    @Resource(name = "DefaultJMSConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(name = "java:jboss/exported/jms/queue/ChatQueue")
    private Destination queue;

    @PostConstruct
    public void init() {
        messages = new ArrayList<>();
    }

    public void onMessage(@Observes Message message) {
        messages.add(message);
        System.out.println(message.getDate().toString() + " " + message.getSender() + ": " + message.getText());
    }
}
