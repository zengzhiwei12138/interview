## redis-cli 操作指令大全_基础篇

### 基本操作命令---连接关闭与状态检查

> 启动redis

	1.使用redis-server启动
	# 切换到redis-server目录 默认在/usr/local/bin目录下
	redis-server
	
	# 带配置文件启动 redis.conf可为自定义目录下
	redis-server redis.conf

	# 带参数启动  日志级别设置为warning
	redis-server redis.conf --loglevel warning 
	
	2.使用redis utils下的redis_init_script脚本启动
	# 笔者是将脚本复制一份命名为redis_6379放于/etc/init.d/redis_6379路径下
	# 关于redis_init_script脚本内容请看笔者另一篇文章《redis常用配置详解》
	/etc/init.d/redis_6379 start

	# 或者 先切换到redis_init_script目录,再启动。请注意：目录每个人可能设置不同！！ 
	cd /etc/init.d/redis_6379  or cd /root/redis-stable/utils
	./redis_6379 start  or ./redis_init_script start	

> 连接redis

	# 连接redis
	# redis-cli -h host -p port
	redis-cli -h 127.0.0.1 -p 6379	

> 检查redis客户端状态
		
	# 检测客户端与redis连接是否正常  结果为PONG则为正常
	redis-cli ping
	
	# 连接redis成功后可执行命令  注意是连接成功后的命令! 连接成功后会有类似如下记录
	# 127.0.0.1:6379>
	# 检测连接状态是否正常  返回PONG则正常
	ping
	
> 关闭redis

	# 停止redis
	redis-cli shutdown

	# 使用redis utils下的redis_init_script脚本停止
	# 参照启动方法
	./redis_init_script stop 




----------


		
### `连接后`可执行命令
 
**注意: 以下命令是正常连接redis之后的可执行命令 连接之后有如127.0.0.1:6379>标识**
 
> 配置
		
	# 日志级别设置为warning
	config set loglevel warning
	
	# 获取日志级别
	config get loglevel
	# =>结果值
	1) "loglevel"
	2) "warning"

> 多数据库

redis是一个**字典结构**的存储服务器,实际上一个Redis实例提供了多个用来存储数据的字典，客户端可以指定将数据存储在哪个字典中。这与我们熟知的在一个关系数据库实例中可以创建多个数据库类似，所以可以将其中的**每个字典都理解成一个独立的数据库**

每个数据库对外都是以一个**从0开始**的递增数字命名，Redis默认支持**16**个数据库，可以通
过配置参数databases来修改这一数字。客户端与Redis建立连接后会自动选择0号数据库，不过
可以随时使用SELECT命令更换数据库，如要选择1号数据库：

	# 选则1号数据库 数据库序号0-15
	select 1
	# 注意:redis默认选中0号数据库,如果数据存储于0号数据库,则在1号数据库获取不到相应的值
	
	# 删除当前数据库所有的键
	flushdb
	# 删除所有数据库的键
	flushall

### 5种数据结构

> 字符串类型

1、 赋值和取值
	
	# 赋值  注意：如果value的值有空格 建议 set key "value"
	set key value
	# 赋多个值  mset zzw 1 zzw2 2 zzw3 3
	mset key value [key value...]	

	# 取值
	get key
	# 取多值 mget zzw zzw1
	mget key [key...]

2、 增加数字
	
	# 返回一个递增的数 如果key不存在 默认为0 所以第一次返回的值为1,不断incr会返回key不断+1的结果值
	incr key
	
	# 增加指定的整数  例如zzw为0 incrby zzw 3 返回的3
	incrby key increment
 
	# 增加指定浮点数  如：zzw为1 incrbyfloat zzw 2.7 返回3.7
	incrbyfloat key increment

3、 减少数字

	# 返回一个递减的数 如果key不存在 默认为0 所以第一次返回的值为-1,不断incr会返回key不断-1的结果值
	decr key
	
	# 减少指定的整数  例如zzw为5 decrby zzw 3 返回的2
	decrby key increment

4、 尾部追加值

	# 尾部追加值
	append key value

5、 获取字符串的长度

	# 获取字符串的长度 一个中文utf-8字符长度是3
	strlen key

6、 查询键

匹配规则如图:
![patterns](https://raw.githubusercontent.com/zengzhiwei12138/interview/master/image/redis-keys-pattern.png)

	# 查看redis所有的key,多行回复  keys pattern
	keys *
	# =>结果值 
	1) "foo"
	2) "zzw"
	
	# 查看匹配的key
	keys zz*
	# =>结果值 1) "zzw"

7、 判断键是否存在

	# 如果key存在 则返回1 不存在则返回0
	exists key

8、 删除键

	# 多键删除之间用空格隔开  del key[key ...]  删除成功返回删除key的数量
	# 单行删除
	del key
	# 多行删除
	del key1 key2
	
	# 由于redis不支持通配符删除，所以我们可以利用linux的管道和xargs命令进行通配符删除
	# 删除所有以zzw开头的键  此命令不需先连接redis 是在不连接redis-cli时执行的
	redis-cli keys zzw* | xargs redis-cli del
	# 删除所有zzw开头的键 此命令不需先连接redis 是在不连接redis-cli时执行的
	redis-cli del redis-cli keys zzw* (待确认 测试不通过 书本命令)

9、 获取键的类型	 
	
	# 获取key键的类型
	type key


> 散列类型

我们现在已经知道Redis是采用字典结构以键值对的形式存储数据的，而散列类型（hash）
的键值也是一种字典结构，其存储了字段（field）和字段值的映射，但字段值只能是字符串，不
支持其他数据类型

1、 赋值和取值

	# 赋值
	hset key filed value
	# 赋多值  如 hmset car price 500 name zzw sizw 10
	hmset key field value [field value...]
	# 字段不存在时赋值 如果字段存在，则不进行任何操作
	hsetnx key field value

	# 取值
	hget key field
	# 取多值
	hmget key field [field...]
	# 取所有的值
	hgetall key
	# 只获取字段名 key对应所有的field
	hkeys key
	# 只获取字段值 key对应所有的value
	hvals key
	# 获取字段数量
	hlen key

2、 判断字段是否存在
	
	# 存在则返回1  不存在则返回0
	hexists key field

3、 增加数字
	
	# field的数字增加increment 
	hincrby key field increment

4、 删除字段
	
	# 删除字段
	hdel key field[field...]

> 列表类型

列表类型内部是使用双向链表（double linked list）实现的，所以向列表两端添加元素的时
间复杂度为0(1)，获取越接近两端的元素速度就越快。这意味着即使是一个有几千万个元素的
列表，获取头部或尾部的10条记录也是极快的

	# 向列表两端增加元素
	# 左添加
	lpush key value[value...]
	# 右添加
	rpush key value[value...]

	# 从列表两端弹出元素 弹出的元素在列表中自动被删除
	# 左弹出
	lpop key
	# 右弹出
	rpop key

	# 获取列表中元素的个数
	llen key

	# 获取列表片段
	lrange key start stop
	# 注意： 从左到右是整数  从右到左是负数  -1代表最后一位
	lrange key 0 2   代表获取前3个数
	lrange key -2 -1 代表获取最后两个数
	lrange key 0 -1  取出所有的数
	
	# 只保留指定片段
	ltrim key start end

	# 删除列表中指定的值
	# 当count＞0时LREM命令会从列表左边开始删除前count个值为value的元素；
	# 当count＜0时LREM 命令会从列表右边开始删除前|count|个值为value的元素；
	# 当count=0是LREM命令会删除所有值为value的元素。例如：
	lrem key count value

	# 设置指定索引的元素值
	lset key index value
	# 获取指定索引的元素值
	lindex key index

	# 向列表中插入元素
	# LINSERT命令首先会在列表中从左到右查找值为pivot的元素，
	# 然后根据第二个参数是BEFORE还是AFTER来决定将value插入到该元素的前面还是后面。
	linsert key before|after pivot value

	# 将元素从一个列表转到另一个列表R source是原 destination是目标
	# RPOPLPUSH是个很有意思的命令，从名字就可以看出它的功能：
	# 先执行RPOP命令再执行LPUSH 命令。RPOPLPUSH命令会先从source列表类型键的右边弹出一个元素，然后将其加入到destination列表类型键的左边，并返回这个元素的值，整个过程是原子的
	rpoplpush source destination
	
> 集合类型

集合类型的常用操作是向集合中加入或删除元素、判断某个元素是否存在等，由于集合类型在Redis内部是使用值为空的散列表（hash table）实现的，所以这些操作的时间复杂度都是0(1)。最方便的是多个集合类型键之间还可以进行并集、交集和差集运算.

	# 增加元素
	# SADD命令用来向集合中增加一个或多个元素，如果键不存在则会自动创建。
	# 因为在一个集合中不能有相同的元素，所以如果要加入的元素已经存在于集合中就会忽略这个元素。
	# 本命令的返回值是成功加入的元素数量（忽略的元素不计算在内）
	sadd key member [member ...]

	# 删除元素
	srem key member [member ...]
	
	# 获取集合所有的元素
	smembers key
	
	# 判断元素是否在集合中
	sismember key member

	# 取多个集合的差集  
	# 如：{1, 2, 3}-{2, 3, 4} 差集为{1}
	#    {2, 3, 4}-{1, 2, 3} 差集为{4}
	# sdiff 命令支持同时传入多个键，例如：sdiff zzw1 zzw2 zzw3
	# 计算顺序是先计算setA-setB，再计算结果与setC的差集。
	sdiff key [key...]

	# 取多个集合的交集
	sinter key[key...]

	# 取多个集合的并集
	sunion key[key...]

	# 获取集合中元素的个数
	scard key
	
	# 进行集合运算并将结果存储
	sdiffstore destination key [key...]
	sinterstore destination key [key...]
	sunionstore destination key [key...]
	
	# 随机获取集合中的元素 count为可选值  也可不输
	# 还可以传递count参数来一次随机获得多个元素，根据count的正负不同，具体表现也不同。
	#（1）当count为正数时，SRANDMEMBER会随机从集合里获得count个不重复的元素。如果count的值大于集合中的元素个数，则SRANDMEMBER会返回集合中的全部元素。
	#（2）当count为负数时，SRANDMEMBER会随机从集合里获得|count|个的元素，这些元素有可能相同
	srandmember key [count]

	# 从集合弹出一个元素并从集合中删除
	spop key

> 有序集合类型
	
	# 增加元素
	# ZADD命令用来向有序集合中加入一个元素和该元素的分数，如果该元素已经存在则会用新的分数替换原有的分数。 分数score不仅可以为整数，也可以为浮点数
	zadd key score member [score member...]
	
	# 获取元素的分数
	zscore key member

	# 获得指定索引范围的元素列表 withscores是可选值
	# 如zrange key 0 -1 所有的分数从小到大排序
	# 从小到大
	zrange key start stop [withscores]
	# 从大到小
	zrevrange key start stop [withscores]

	# 获得指定分数范围的元素
	# 如果希望分数范围不包含端点值，可以在分数前加上 ( 符号
	# min和max还支持无穷大，同ZADD命令一样，-inf 和+inf分别表示负无穷和正无穷
	# offset代表索引开始值 count代表数量
	zrangebyscore key min max [withscores] [limit offset count]

	# 增加某个元素的分数 返回增加后的结果
	zincrby key increment member

	# 获取集合中元素的个数
	zcard key
	
	# 获得指定分数范围內的元素个数
	zcount key min max

	# 删除一个或多个元素
	zrem key member [member...]
	# 按照索引删除元素
	zremrangebyrank key start stop
	# 按照分数范围删除元素
	zremrangebyscore key min max

	# 获取元素的分数排名
	# 分数最小排名为0
	zrank key member
	# 分数最大排名为0
	zrevrank key member 

	# 计算有序集合的交集 numkeys为参与计算的集合的个数
	zinterstore destination numkeys key [key...] [weights weight [weight...]][agregate sum|min|max]
	# 解释：zinterstore命令用来计算多个有序集合的交集并将结果存储在destination键中（同样以有序集合类型存储），返回值为destination键中的元素个数。destination键中元素的分数是由agregate参数决定的
	#（1）当agregate是sum时(默认值），destination键中元素的分数是每个参与计算的集合中该元素分数的和。
	#（2）当agregate是min时，destination键中元素的分数是每个参与计算的集合中该元素分数的最小值。
	#（3）当agregate是max时，destination键中元素的分数是每个参与计算的集合中该元素分数的最大值。

	# 计算有序集合的并集 zunionstore 同上，不再详细介绍
	


	

注意：redis的所有数据类型都不支持数据类型嵌套，比如集合类型的每个元素都只能是字符串，不能是另一个集合或散列表等。	

	
	