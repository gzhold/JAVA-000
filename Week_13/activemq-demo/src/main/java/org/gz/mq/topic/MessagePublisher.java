package org.gz.mq.topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessagePublisher {

    private static final int SEND_NUM = 10;
    private static final String BROKER_URL = "tcp://10.1.60.101:61616";
    private static final String DESTINATION = "test.topic.demo";

    private static void sendMessage(final Session session, final MessageProducer producer) throws Exception {
        for (int i = 0; i < SEND_NUM; i++) {
            String message = "send num" + (i + 1);
            TextMessage text = session.createTextMessage(message);
            text.setIntProperty("id", i);
            System.out.println(String.format("Sent %d messages : %s", i, message));
            producer.send(text);
        }
    }

    private static void run() throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD);
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(DESTINATION);

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        sendMessage(session, producer);

        producer.send(session.createTextMessage("SHUTDOWN"));
        Thread.sleep(1000 * 3);
        connection.close();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        run();
    }

}
