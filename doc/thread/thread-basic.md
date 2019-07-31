## 线程并发基础知识点

### 线程的状态切换

线程的状态有以下6种状态

![img](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/19-1-29/Java%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%8A%B6%E6%80%81.png)

- new     创建后尚未启动
- runnable    可能正在运行  也可能在等待CPU时间片
- blocked      等待获取一个排它锁，如果其线程释放了锁就会结束此状态
- time waiting   无需等待其它线程显式地唤醒，在一定时间之后会被系统自动唤醒
- waiting       等待其它线程显式地唤醒，否则不会被分配 CPU 时间片
- terminated     可以是线程结束任务之后自己结束，或者产生了异常而结束

![img](https://github.com/CyC2018/CS-Notes/raw/master/notes/pics/adfb427d-3b21-40d7-a142-757f4ed73079.png)

![img](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/19-1-29/Java+%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81%E5%8F%98%E8%BF%81.png)

### 实现线程的方法

- 实现 Runnable 方法
- 实现 Callable 方法
- 继承 Thread 类

> 实现 Callable 方法 vs Runnable

1. Callable规定的方法是call()，而Runnable规定的方法是run()
2. Callable的任务执行后可返回值，而Runnable的任务是不能返回值的
3. call()方法可抛出异常，而run()方法是不能抛出异常的
4. 运行Callable任务可拿到一个Future对象， Future表示异步计算的结果

> 实现接口 vs 继承 Thread

实现接口会更好一些  因为

1. Java不支持多重继承 因此继承了 Thread 类就无法继承其他类  但是可以实现多个接口
2. 继承整个 Thread 类开销过大  Thread 源码也是继承 Runnable接口

### Synchronized 关键字

synchronized的底层原理是基于JVM层面的

synchronized关键字的使用 参照链接详解 [synchronized的三种用法](https://github.com/CyC2018/CS-Notes/blob/master/notes/Java%20并发.md#五互斥同步)

- 修饰实例方法  
- 修饰静态方法
- 修饰代码块

总结: 

1. synchronized 修饰静态方法和 *.class 是给class类加锁   
2. synchronized 修饰实例方法和this 是给当前调用方法的实例对象加锁

应用: [synchronized在单例模式中的应用](https://snailclimb.gitee.io/javaguide/#/java/Multithread/JavaConcurrencyAdvancedCommonInterviewQuestions?id=_12-说说自己是怎么使用-synchronized-关键字，在项目中用到了吗)

javap -c -l -s -v 可以查看

- **synchronized 同步语句块的实现使用的是 monitorenter 和 monitorexit 指令，其中 monitorenter 指令指向同步代码块的开始位置，monitorexit 指令则指明同步代码块的结束位置。** 当执行 monitorenter 指令时，线程试图获取锁也就是获取 monitor(monitor对象存在于每个Java对象的对象头中，synchronized 锁便是通过这种方式获取锁的，也是为什么Java中任意对象可以作为锁的原因) 的持有权。当计数器为0则可以成功获取，获取后将锁计数器设为1也就是加1。相应的在执行 monitorexit 指令后，将锁计数器设为0，表明锁被释放。如果获取对象锁失败，那当前线程就要阻塞等待，直到锁被另外一个线程释放为止。
- synchronized 修饰的方法并没有 monitorenter 指令和 monitorexit 指令，取得代之的确实是 ACC_SYNCHRONIZED 标识，该标识指明了该方法是一个同步方法，JVM 通过该 ACC_SYNCHRONIZED 访问标志来辨别一个方法是否声明为同步方法，从而执行相应的同步调用。

### ReentrantLock

ReentrantLock 是 java.util.concurrent（J.U.C）包中的锁

### Synchronized vs ReentrantLock

1. 锁的实现

   synchronized 是基于JVM实现的  ReentrantLock是基于JDK实现的

2. 等待可中断

   ReentrantLock可以中断   Synchronized不可中断

   中断 : 当持有锁的线程长期不释放锁的时候   正在等待的线程可选择放弃等待 改为处理其他事情

3. 公平锁

   公平锁是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来依次获得锁。(哪个线程先等待 哪个线程先获取锁)

   synchronized 中的锁是非公平的，ReentrantLock 默认情况下也是非公平的，但是也可以是公平的。

4. 锁绑定多个条件

   一个 ReentrantLock 可以同时绑定多个 Condition 对象。

5. 性能

   新版本 Java 对 synchronized 进行了很多优化，例如自旋锁等，synchronized 与 ReentrantLock 大致相同。

### volatile

