> ArrayList

- ArrayList 内部是通过数组实现的  它允许对元素进行快速随机访问

- 当数组大小超过所允许的存储能力时  ArrayList以 原数组大小 + 原数组大小的 0.5 进行扩容
- 当从ArrayList中间位置插入或删除元素 需要对数组进行复制 移动  代价比较高  因此  它适合随机查找和遍历  不适合插入和删除 

> Vector

- Vector 内部也是通过数组实现的  
- Vector 对涉及到数组插入,删除等操作加了 synchronized 关键字  加锁  实现线程同步
- 实现同步需要很高的消耗  因此  它访问比访问ArrayList慢

> LinkedList

- LinkedList 是用链表结构存储数据的  很适合数据的动态插入和删除  随机遍历和查找速度比较慢