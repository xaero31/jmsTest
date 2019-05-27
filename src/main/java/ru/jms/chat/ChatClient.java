package ru.jms.chat;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Nikita Ermakov
 */
public class ChatClient {

    private static final String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";

    private static final String DESTINATION = "jms/queue/ChatQueue";

    public static void main(String[] args) throws IOException, NamingException {
        final Context namingContext = getContext();
        System.out.println("context done");
        final ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(CONNECTION_FACTORY);
        System.out.println("factory done");
        final Destination destination = (Destination) namingContext.lookup(DESTINATION);
        System.out.println("destination done");

        final JMSContext jmsContext = connectionFactory.createContext("newUser", "password");
        final JMSProducer producer = jmsContext.createProducer();
        final JMSConsumer consumer = jmsContext.createConsumer(destination, "source = 'server'");

        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    System.out.println("msg: " + ((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Print message");
        try(Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                final String text = sc.nextLine();
                producer.setProperty("source", "client").send(destination, text);
                System.out.println("Print message");
            }
        }
    }

    private static Context getContext() throws NamingException, IOException {
        final Properties env = new Properties();
        env.load(ChatClient.class.getClassLoader().getResourceAsStream("wildfly-jndi.properties"));
        return new InitialContext(env);
    }
}
