package azj.zzw.interview.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see ActiveMQConnectionFactory
 * @since 2019/7/5 0005-14:38
 */
public class Provider {

    public static void main(String[] args) {
        try {
            Session session = MqUtils.createConnectAndSession("tcp://192.168.124.128:61616");
            Destination destination = session.createQueue("first-queue");
            // 创建消息提供者
            MessageProducer producer = session.createProducer(destination);
            // 创建消息
            TextMessage textMessage = session.createTextMessage("I test the study of the active mq");
            //　发送消息
            producer.send(textMessage);
            // 事务提交
            session.commit();
            // 关闭会话
            session.close();
            // 关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
