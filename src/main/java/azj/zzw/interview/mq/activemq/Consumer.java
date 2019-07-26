package azj.zzw.interview.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ActiveMQConnectionFactory
 * @since 2019/7/5 0005-15:44
 */
public class Consumer {

    public static void main(String[] args) {
        try {
            Session session = MqUtils.createConnectAndSession("tcp://192.168.124.128:61616");
            Destination destination = session.createQueue("first-queue");
            // 创建消费者
            MessageConsumer consumer = session.createConsumer(destination);
            // 接收消息
            TextMessage textMessage = (TextMessage) consumer.receive();
            // 打印数据
            System.out.println("the message consumer receive is [" + textMessage.getText() + "]");
            // 提交事务
            session.commit();
            // 关闭会话
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
