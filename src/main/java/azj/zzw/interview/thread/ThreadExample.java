package azj.zzw.interview.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;

/**
 * 线程基础和进阶知识学习
 *
 * @author zzw zengzhiwei_hfut@163.com
 * @see Runnable
 * @since 2019/7/31 0031-10:42
 */
public class ThreadExample {

    public static void main(String[] args) throws Exception {
        // 初始化线程池 参见阿里巴巴开发公约  程序员应该显示的初始化线程池 而不应该调用 executors 的方法
        // Executors.newFixedThreadPool(10); 该种方法需要pass 因为该种方法创建的线程池可接收的线程 task 队列为 Integer.MAX_VALUE  可能导致OOM

        // positive example 1
        // 线程的命名工厂
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").setDaemon(true).build();
        // 应该显示的初始化线程池 更好的管理线程
        ExecutorService threadPool = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(30), nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        // 执行自定义线程
        threadPool.execute(new MyRunnable());
        Future future = threadPool.submit(new MyCallable());
        System.out.println(future.get());
        // 等待线程执行完毕
        TimeUnit.SECONDS.sleep(10);
        // 优雅的关闭线程池
        threadPool.shutdown();
    }


    private static class MyRunnable implements Runnable {

        @Override
        public void run(){
            System.out.println("test runnable");
        }
    }


    private static class MyCallable implements Callable {

        @Override
        public Object call() throws Exception {
            // 获取线程管理对象
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
            // 不需要获取同步的 monitor 和 synchronized 信息，仅获取线程和线程堆栈信息
            ThreadInfo[] threadInfos = mxBean.dumpAllThreads(false, false);
            // 遍历线程信息，仅打印线程 ID 和线程名称信息
            for (ThreadInfo threadInfo : threadInfos) {
                System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
            }
            System.out.println("this is my first executors and the first threadPoolExecutor example");
            return "awesome";
        }
    }
}
