package ru.jms.chat;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Nikita Ermakov
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/exported/jms/queue/ChatQueue"),
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "source = 'client'")})
public class Receiver implements MessageListener {

    @Inject
    private Event<Message> sendMessageEvent;

    @Override
    public void onMessage(javax.jms.Message message) {
        try {
            if (message instanceof TextMessage) {
                final String text = ((TextMessage) message).getText();
                sendMessageEvent.fire(new Message(text, "Client"));
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
