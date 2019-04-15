## redis常用配置详解

### redis_init_script
[redis_init_script---redis初始化脚本](https://github.com/antirez/redis/blob/unstable/utils/redis_init_script) @感兴趣的同学自行查阅
	
	# 设置redis监听端口号
	REDISPORT=6379
	# 设置redis-server路径
	EXEC=/usr/local/bin/redis-server
	# 设置redis-cli路径	
	CLIEXEC=/usr/local/bin/redis-cli
	# 设置pid文件生成路径  
	# redis以守护进程启动时,redis会在该目录下生成redis_端口号.pid文件 详见下文解释
	PIDFILE=/var/run/redis_${REDISPORT}.pid
	# redis.conf配置文件的路径
	CONF="/etc/redis/${REDISPORT}.conf"
	
### redis.conf

[官方redis配置文件](https://github.com/antirez/redis/blob/unstable/redis.conf) @推荐英文好的同学研读原版

	# 设置为守护进程启动 守护进程的含义是在系统后台运行
	# 默认是非守护进程运行,则关闭终端连接后redis就会停止运行
	# 建议开启守护进程
	daemonize yes
	
	# 当redis设置为守护进程启动时,redis会在/var/run目录下生成一个pid文件
	# 此处设置生成的redis_端口号.pid的文件路径 
	pidfile /var/run/redis_端口号.pid

	# 设置redis监听的端口号 默认为6379
	port 6379
	
	# 设置redis持久化文件存放位置
	dir /var/redis/端口号

### 常见问题
	