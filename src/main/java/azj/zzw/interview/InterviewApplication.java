package azj.zzw.interview;

import azj.zzw.interview.distribute.zookeeper.CuratorDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zzw zengzhiwei_hfut@163.com
 * @see SpringApplication
 * @since 2019/4/9 0009-17:39
 */
@SpringBootApplication
public class InterviewApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(InterviewApplication.class, args);
        CuratorDemo.main(args);
    }

}
