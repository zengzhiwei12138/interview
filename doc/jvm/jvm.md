### 运行时数据区

### 垃圾收集算法

> 复制回收算法

不足 : 内存开销大  资源浪费

>  标记-清除算法    

不足

- 标记和清除的过程效率都不高
- 会产生大量的内存碎片

> 标记-整理算法

不会产生内存碎片  但是需要移动大量对象  处理效率低

### 垃圾收集器

![img](https://github.com/CyC2018/CS-Notes/raw/master/notes/pics/c625baa0-dde6-449e-93df-c3a67f2f430f.jpg)

>  新生代 : 复制回收算法

- serial 收集器  串行  单线程
- parNew 收集器 serial的多线程版本
- Parallel Scavenge 收集器   多线程   吞吐量优先收集器  

>  老年代 : 标记-清除算法 或 标记-整理算法

- serial old  
- parallel old 
- cms