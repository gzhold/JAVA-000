package org.gz.mq.topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {

    private static final String BROKER_URL = "tcp://10.1.60.101:61616";
    private static final String DESTINATION = "test.topic.demo";

    public static void run() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(DESTINATION);
        MessageConsumer consumer = session.createConsumer(destination);
        System.out.println("Waiting data...");
        while (true) {
            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                String body = ((TextMessage) msg).getText();
                if ("SHUTDOWN".equals(body)) {
                    connection.close();
                    System.exit(0);
                } else {
                    System.out.println(String.format("Received %s messages.", ((TextMessage) msg).getText()));
                }
            } else {
                System.out.println("Unexpected message type: " + msg.getClass());
            }
        }
    }

    public static void main(String[] args) throws JMSException {
        run();
    }

}
