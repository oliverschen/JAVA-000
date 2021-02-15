### 第 24 课作业

1. **(必做)** 搭建ActiveMQ服务，基于JMS，写代码分别实现对于queue和topic的消息 

生产和消费，代码提交到github。 

#### Docker搭建

```json
docker run -d --name activemq1 -p 61616:61616 -p 8161:8161 rmohr/activemq
```

##### 登录管理界面

```http
http://localhost:8161/admin/
```

#### 使用

##### provider

```java
/**
 * @author ck
 */
@Slf4j
public class Producer {


    public static void main(String[] args) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = null;
        Session session;
        MessageProducer producer;

        try {
            connection = factory.createConnection();
            // 开启连接
            connection.start();
            //AUTO_ACKNOWLEDGE 自动签收
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            // queue
//            Queue destination = session.createQueue(CK_MSG);
            // topic 
            Topic destination = session.createTopic(CK_MSG_TOPIC);
            producer = session.createProducer(destination);
            for (int i = 0; i < 5; i++) {
                String msg = "my id is : " + i;
                TextMessage textMessage = session.createTextMessage(msg);
                producer.send(textMessage);
                log.info("send success, id is :" + i);
            }
            session.commit();
        }catch (JMSException e) {
            e.printStackTrace();
            log.error("provider send error !");
        }finally {
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
```

##### consumer

```java
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

//            Queue destination = session.createQueue(CK_MSG);
            Topic destination = session.createTopic(CK_MSG_TOPIC);

            MessageConsumer consumer = session.createConsumer(destination);

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
```

#### 结果

##### provider

```bash
21:35:07.981 [main] DEBUG org.apache.activemq.TransactionContext - Begin:TX:ID:chenkui.local-56293-1612791307801-1:1:1
21:35:07.983 [main] INFO com.github.oliverschen.mq.Producer - send success, id is :0
21:35:07.983 [main] INFO com.github.oliverschen.mq.Producer - send success, id is :1
21:35:07.984 [main] INFO com.github.oliverschen.mq.Producer - send success, id is :2
21:35:07.984 [main] INFO com.github.oliverschen.mq.Producer - send success, id is :3
21:35:07.984 [main] INFO com.github.oliverschen.mq.Producer - send success, id is :4
```

##### consumer

```bash
21:35:03.339 [main] INFO com.github.oliverschen.mq.Consumer - wait message...
21:35:08.016 [main] INFO com.github.oliverschen.mq.Consumer - message info:::my id is : 0
21:35:08.016 [main] INFO com.github.oliverschen.mq.Consumer - message info:::my id is : 1
21:35:08.016 [main] INFO com.github.oliverschen.mq.Consumer - message info:::my id is : 2
21:35:08.016 [main] INFO com.github.oliverschen.mq.Consumer - message info:::my id is : 3
21:35:08.017 [main] INFO com.github.oliverschen.mq.Consumer - message info:::my id is : 4
```

### 第 25 课作业

1. **(必做)** 搭建一个3节点Kafka集群，测试功能和性能；实现spring kafka下对kafka集群 

的操作，将代码提交到github。 

#### Docker 搭建

##### kafka

```bash
docker run -d --name zookeeper --publish 2181:8181 --volume ~/develop/docker/app/zookeeper:/etc/localtime zookeeper:latest
```

```bash
docker run -d --name kafka --publish 9092:9092 \
--link zookeeper \
--env KAFKA_ZOOKEEPER_CONNECT=192.168.0.105:2181 \
--env KAFKA_ADVERTISED_HOST_NAME=192.168.0.105 \
--env KAFKA_ADVERTISED_PORT=9092  \
wurstmeister/kafka
```

##### 集群

kafka 集群暂时没有搭建，后面有空余时间完善*

#### 使用







