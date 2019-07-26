package azj.zzw.interview.mq.kafka;

import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * 等待最帅的添加描述
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see KafkaConsumer
 * @since 2019/7/14 0014-21:54
 */
@Slf4j
public class Consumer extends ShutdownableThread {

    /**
     * consumer
     */
    private final KafkaConsumer<Integer,String> consumer;


    public Consumer() {
        super("KafkaConsumerTest",false);
        Properties props = new Properties();
        // 集群地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.KAFKA_BROKERS_CLUSTER);
        // group id 消息所属的分组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDemo");
        // 是否自动提交消息 offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 自动提交的间隔时间
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 设置使用最开始的offset偏移量为当前group.id的最早消息
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 设置心跳时间
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // 对key和value设置反序列化对象
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(KafkaConstant.TOPIC_NAME));
        ConsumerRecords<Integer, String> records = consumer.poll(1000);
        for(ConsumerRecord record : records){
            log.info("the partition [{}] receive message [{}],the offset is [{}]",record.partition(),record.value(),record.offset());
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.start();
    }
}
