package azj.zzw.interview.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Session;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see 等待最帅的添加
 * @since 2019/7/5 0005-15:56
 */
public class MqUtils {

    private MqUtils(){
    }

    public static Session createConnectAndSession(String url) throws Exception{
        // 连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        // 创建连接
        Connection connection = connectionFactory.createConnection();
        // 启动连接
        connection.start();
        // 创建会话
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        return session;
    }
}
