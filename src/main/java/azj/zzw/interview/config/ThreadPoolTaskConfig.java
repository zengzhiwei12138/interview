package azj.zzw.interview.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * springboot配置ThreadPoolTaskExecutor
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
 * @since 2019/4/24 0024-11:10
 */
@Configuration
@ConfigurationProperties(prefix = "thread")
public class ThreadPoolTaskConfig {

    private String name;

    @Bean(name = "executor")
    public Executor executor(){
        // 执行计划
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(1);
        // 设置最大线程数
        executor.setMaxPoolSize(1);
        // 设置除核心线程外的线程存活时间 单位秒
        executor.setKeepAliveSeconds(3);
        // 设置线程池队列 如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(100);
        // 设置线程名称前缀
        executor.setThreadNamePrefix(name);
        // 设置拒绝策略
        // 4种拒绝策略  参考文章 https://www.cnblogs.com/skywang12345/p/3512947.html
        // AbortPolicy         -- 当任务添加到线程池中被拒绝时，它将抛出 RejectedExecutionException 异常。
        // CallerRunsPolicy    -- 当任务添加到线程池中被拒绝时，会在线程池当前正在运行的Thread线程池中处理被拒绝的任务。
        // DiscardOldestPolicy -- 当任务添加到线程池中被拒绝时，线程池会放弃等待队列中最旧的未处理任务，然后将被拒绝的任务添加到等待队列中。
        // DiscardPolicy       -- 当任务添加到线程池中被拒绝时，线程池将丢弃被拒绝的任务。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}


