### 参考链接

[廖雪峰sql教程](https://www.liaoxuefeng.com/wiki/1177760294764384/1218728442198976)



### 主键

```sql
-- 添加主键
alter table table_name add primary key column_name;
```

#### 主键选取的原则

- 基本原则 : 不使用任何业务相关的字段作为主键   因此，身份证号、手机号、邮箱地址这些看上去可以唯一的字段，均不可用作主键。
- 尽量不使用联合主键

### 外键

```sql
-- 添加外键
alter table table_name1 add constraint key_name foreign key (column_name1) references table_name2 (column_name2);

-- 删除外键
alter table table_name drop foreign key key_name;
```

通过定义外键约束  可以保证无法插入无效的数据

### 索引

- 索引的效率取决于索引列的值是否散列   即该列的值如果互不相同  那么索引效率越高    反之  如果记录的列存在大量相同的值  例如性别   因此对性别gender创建索引就没有意义
- 索引的优点是提高了查询效率    缺点是在插入/更新/删除记录时 需要同时修改索引   因此  索引越多   插入/更新/删除记录的速度就越慢
- 对于主键  关系数据库会自动对其创建主键索引   使用主键索引的效率是最高的   因为主键是绝对唯一

```sql
-- 添加索引
alter table table_name add index index_name (column1,column2...);
```

#### 唯一索引

```sql
-- 添加唯一索引
alter table table_name add unique index index_name (column);

-- 添加唯一约束   添加唯一约束并没有添加唯一索引  只是保证字段唯一性
alter table table_name add constraint uni_name unique (column);
```

### 查询 select

#### 基本查询

```
-- 查询数据库表数据
select * from table_name;

-- 用于计算或者测试数据库是否连接
select 200+300;
select 1;
```

#### 条件查询

```sql
-- 条件查询  大于小于等等
select * from table_name where <表达式>;

-- AND OR NOT  example   not查询不是很常用
select * from student where score >= 80 and gender = 'M'; 

select * from student where score >= 80 or gender = 'M';

select * from student where not name = 'sql'
```

#### 投影查询

select 仅返回指定列  这种操作叫投影

```sql
-- 查询出固定的列
select 列1,列2 别名,列3... from <表名>;

-- example
select id,score points,name from student;
```

#### 排序

```sql
-- 由低到高排序  默认 asc
select * from table_name order by column <asc>;

-- 倒序  由高到低排序  desc
select * from table_name order by column desc;

-- 多条件排序
select * from table_name order by column1 desc,column2;

-- 如果有where关键字  order by 应该放在where后面
select * from table_name where <条件> order by column;
```

#### 分页查询

```sql
-- 分页查询  limit 表示 pageSize 每页显示的结果数   offset表示pageStart  当前页的索引
-- limit = pageSize   offset = (pageStart-1)*pageSize
-- 随着N越来越大  查询效率也会越来越低
limit <M> offset <N>
-- 简写
limit 15 offset 30   简写为   limit 30,15
```

#### 聚合查询

- 聚合查询的列中，只能放入分组的列

```sql
-- 总数  使用聚合查询并设置结果集的列名为num
select count(*) num from table_name;
-- 和
select sum(column) from table_name;
-- 平均
select avg(column) average from table_name where <条件>;
-- 最大 MAX  最小  MIN
-- 分组  根据分组查询数据
group by  
select count(*) from table_name group by column;
```

#### 多表查询

```sql
-- 多表查询又称笛卡尔查询
select * from <表1> <表2>
```

#### 连接查询

![img](https://www.liaoxuefeng.com/files/attachments/1246892164662976/l)

- 内连接   只返回同时存在于两张表的行数据

  - 先确定主表    使用 from <表1>
  - 再确定连接的表  使用 inner join <表2>
  - 然后确定连接条件  使用 on <条件...>
  - 可选  加上 where  /  order  by 等子句

- 外连接

  - 左(外)连接   返回左表都存在的行   如果某一行仅在左表存在，那么结果集就会以`NULL`填充剩下的字段

    ![img](https://www.liaoxuefeng.com/files/attachments/1246893588481376/l)

  - 右(外)连接   右表都存在的行。如果某一行仅在右表存在，那么结果集就会以`NULL`填充剩下的字段

    ![img](https://www.liaoxuefeng.com/files/attachments/1246893609222688/l)

  - 全连接    它会把两张表的所有记录全部选择出来，并且，自动把对方不存在的列填充为`NULL`

    ![img](https://www.liaoxuefeng.com/files/attachments/1246893632359424/l)

```sql
-- 内连接
select s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
from  students s
inner join classes c
on s.class_id = c.id;

-- 左连接
select s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
from students s
left outer join classes c
on s.class_id = c.id;

-- 右连接
select s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
from students s
right outer join classes c
on s.class_id = c.id;

-- 全连接
select s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
from students s
right outer join classes c
on s.class_id = c.id;
```

### 修改数据

#### 插入数据 insert

```sql
-- 插入一条数据
insert into <表名> (字段1,字段2...) values (值1,值2...);

-- 插入多条数据
insert into students (class_id, name, gender, score) values
  (1, '大宝', 'M', 87),
  (2, '二宝', 'M', 81);
```

### 事务

把多条语句作为一个整体进行操作的功能 称为数据库事务   数据库事务可以确保该事务范围内的所有操作都可以全部成功或者全部失败

- 隐式事务 : 对于单条SQL语句   数据库系统自动将其作为一个事务执行  这种事务被称为`隐式事务`
- 显式事务 : 要手动把多条SQL语句作为一个事务执行  使用BEGIN开启一个事务  使用COMMIT提交一个事务  这种事务被称为`显式事务`  有些时候  我们希望主动让事务失败 可以使用ROLLBACK回滚事务

数据库事务ACID的四大特性

- Atomic          原子性  将所有SQL作为原子工作单元执行 要么全部执行  要么全部不执行
- Consistent   一致性  事务完成后  所有的数据状态都是一致的
- Isolation       隔离性  如果有多个事务并发执行   每个事务作出的修改必须与其他事务隔离
- Duration       持久性  即事务完成后  对数据库数据的修改被持久化存储

