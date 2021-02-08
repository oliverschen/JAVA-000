package com.github.oliverschen.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import static com.github.oliverschen.mq.Const.BROKER_URL;
import static com.github.oliverschen.mq.Const.CK_MSG;

/**
 * @author ck
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) {


        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = null;
        Session session;

        try {
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(CK_MSG);
            MessageConsumer consumer = session.createConsumer(queue);

            while (true) {
                TextMessage receive = (TextMessage) consumer.receive(2000);
                if (receive != null) {
                    log.info("message info:::" + receive.getText());
                }else {
                    log.info("wait message...");
                    Thread.sleep(3000);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
            log.error("receive message error");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
