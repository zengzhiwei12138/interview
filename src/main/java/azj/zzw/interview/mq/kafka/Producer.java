package azj.zzw.interview.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;

import java.io.IOException;
import java.util.Properties;

/**
 * 等待最帅的添加描述
 *
 * Properties props = new Properties();
 * props.put("bootstrap.servers", "localhost:9092");
 * props.put("acks", "all");
 * props.put("retries", 0);
 * props.put("batch.size", 16384);
 * props.put("linger.ms", 1);
 * props.put("buffer.memory", 33554432);
 * props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
 * props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see org.apache.kafka.clients.producer.KafkaProducer
 * @since 2019/7/14 0014-21:03
 */
@Slf4j
public class Producer {

    /**
     * producer
     */
    private final KafkaProducer<Integer,String> producer;

    /**
     * 构造方法
     * 初始化producer的一些参数
     */
    public Producer(){
        Properties props = new Properties();
        // kafka集群地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.KAFKA_BROKERS_CLUSTER);
        // key的类型
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        // value的类型
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(CommonClientConfigs.CLIENT_ID_CONFIG, "producerDemo");
        this.producer = new KafkaProducer<>(props);
    }

    /**
     * send the message
     */
    public void sendMsg(){
        producer.send(new ProducerRecord<>(KafkaConstant.TOPIC_NAME, 1,"message"), (metadata, exception) -> {
            log.info("message is send to [{}] partition,the offset is [{}]",metadata.partition(),metadata.offset());
        });
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.sendMsg();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
