### redis支持哪几种数据类型

redis一共支持5种数据类型

- 字符串类型  string 
- 散列类型 hash  
- 列表类型 list 双向列表
- 集合类型 set 
- 有序集合类型 zset sorted set

### redis的优缺点

> 知识点
>
> 原子性的定义: 如果把一个事务看作是一个程序  它要么完整的被执行 要么完全不执行  这种特性叫原子性
>
> 例子: A 想要从自己的帐户中转 1000 块钱到 B 的帐户里。那个从 A 开始转帐，到转帐结束的这一个过程，称之为一个事务。如果在 A 的帐户已经减去了 1000 块钱的时候，忽然发生了意外，比如停电什么的，导致转帐事务意外终止了，而此时 B 的帐户里还没有增加 1000 块钱。那么，我们称这个操作失败了，要进行回
> 滚。回滚就是回到事务开始之前的状态，也就是回到 A 的帐户还没减 1000 块的状态，B 的帐户的原来的状态。此时A 的帐户仍然有 3000 块，B 的帐户仍然有 2000 块。我们把这种要么一起成功（A 帐户成功减少 1000，同时 B 帐户成功增加 1000），要么一起失败（A 帐户回到原来状态，B 帐户也回到原来状态）的操作叫原子性操作。

- 优点
  - 性能极高  redis 支持超过100k+每秒的读写频率
  - 丰富的数据类型   redis支持 字符串\散列\列表\集合\有序集合五种数据类型操作
  - 原子性  redis所有的操作都是原子性的  同时redis支持事务  还支持几个操作合并后的原子性执行
  - 丰富的特性   redis还支持publish/subscribe  / 通知 / key 过期等等特性

- 缺点      [面试官常问：Redis主从是如何同步的？](https://zhuanlan.zhihu.com/p/56579802)
  - 由于是内存数据库  所以单台机器   存储的数据量跟机器本身的内存大小有关  虽然redis有key过期策略 但还是需要提前预估和节约内存   如果内存增长过快  需要定期删除策略
  - 如果进行完整重同步  由于需要生成rdb文件  并进行传输  会占用主机CPU  并会消耗网络带宽   虽然redis2.8版本之后  已经有部分重同步功能  但是还是有可能有完整重同步的  比如 新上线的备机
  - 修改配置文件  进行重启  将磁盘中的数据加载到内存中  时间较久   在这个过程中  redis不能提供服务

### redis分布式锁的实现

#### a single instance

基于redis 2.6.12版本之后  redis为set命令增加一系列选项

- 添加锁   该加锁方式在redis集群中可能存在问题   A客户端在Redis的master节点上拿到了锁，但是这个加锁的key还没有同步到slave节点，master故障，发生故障转移，一个slave节点升级为master节点，B客户端也可以获取同个key的锁，但客户端A也已经拿到锁了，这就导致多个客户端都拿到锁

```sh
# 该命令来源于官网 
# resource_name 锁名称
# my_random_value 锁的值
# NX: 仅当key存在时设置值
# PX milliseconds: 设定过期时间，单位为毫秒 NX: 仅当key不存在时设置值
set resource_name my_random_value NX PX 30000

# jedis对该命令做了相关实现
jedis.set(final String key, final String value, final String nxxx, final String expx,
      final int time)
```

- 释放锁

```sh
# lua表达式 原子性操作
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```

#### The Redlock

(翻译于官网) antirez提出的redlock算法大概是这样的：

在Redis的分布式环境中，我们假设有N个Redis master。这些节点**完全互相独立，不存在主从复制或者其他集群协调机制**。我们确保将在N个实例上使用与在Redis单实例下相同方法获取和释放锁。现在我们假设有5个Redis master节点，同时我们需要在5台服务器上面运行这些Redis实例，这样保证他们不会同时都宕掉。

为了取到锁，客户端应该执行以下操作:

- 获取当前Unix时间，以毫秒为单位。
- 依次尝试从5个实例，使用相同的key和**具有唯一性的value**（例如UUID）获取锁。当向Redis请求获取锁时，客户端应该设置一个网络连接和响应超时时间，这个超时时间应该小于锁的失效时间。例如你的锁自动失效时间为10秒，则超时时间应该在5-50毫秒之间。这样可以避免服务器端Redis已经挂掉的情况下，客户端还在死死地等待响应结果。如果服务器端没有在规定时间内响应，客户端应该尽快尝试去另外一个Redis实例请求获取锁。
- 客户端使用当前时间减去开始获取锁时间（步骤1记录的时间）就得到获取锁使用的时间。**当且仅当从大多数**（N/2+1，这里是3个节点）**的Redis节点都取到锁，并且使用的时间小于锁失效时间时，锁才算获取成功**。
- 如果取到了锁，key的真正有效时间等于有效时间减去获取锁所使用的时间（步骤3计算的结果）。
- 如果因为某些原因，获取锁失败（没有在至少N/2+1个Redis实例取到锁或者取锁时间已经超过了有效时间），客户端应该在**所有的Redis实例上进行解锁**（即便某些Redis实例根本就没有加锁成功，防止某些节点获取到锁但是客户端没有得到响应而导致接下来的一段时间不能被重新获取锁）。

### 参考资料

- [基于redis的分布式锁的实现](https://juejin.im/post/5cc165816fb9a03202221dd5)
- [redLock](https://redis.io/topics/distlock)
- [Redlock：Redis分布式锁最牛逼的实现](https://mp.weixin.qq.com/s?__biz=MzU5ODUwNzY1Nw==&mid=2247484155&idx=1&sn=0c73f45f2f641ba0bf4399f57170ac9b&scene=21#wechat_redirect)
- [redisson](https://github.com/redisson/redisson)