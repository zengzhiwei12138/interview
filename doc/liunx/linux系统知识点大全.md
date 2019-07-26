## Linux系统知识点大全

### linux系统目录结构
![linux目录结构](https://raw.githubusercontent.com/zengzhiwei12138/interview/master/image/linux目录结构.png)

### 文件操作

> 切换目录 cd

	cd app	切换到app目录 
	cd ..	切换到上一层目录 
	cd /	切换到系统根目录
	cd ~	切换到用户主目录
	cd -	切换到上一个所在目录

> 列出文件列表

	ls      横向列表
 	ls -a   显示所有文件或目录（包含隐藏的文件）
	ls -l   缩写成ll 纵向列表
	ll -ltr 根据文件更新时间排序,最后更新的文件显示在最下面
> 创建目录
	
	mkdir app           在当前目录下创建app目录 
	mkdir –p app2/test  级联创建aap2以及test目 

> 移除目录



> 解包和打包-tar 

tar命令位于/bin目录下，它能够将用户所指定的文件或目录打包成一个文件，但不做压缩。一般Linux上常用的压缩方式是选用tar将许多文件打包成一个文件，再以gzip压缩命令压缩成xxx.tar.gz(或称为xxx.tgz)的文件
	
	# 常用参数：
	 -c：创建一个新tar文件
	 -v：显示运行过程的信息
	 -f：指定文件名
	 -z：调用gzip压缩命令进行压缩
	 -t：查看压缩文件的内容
	 -x：解开tar文件
	# 打包：
	tar –cvf xxx.tar ./*
	# 打包并且压缩：
	tar –zcvf xxx.tar.gz ./* 

	# 解压 
    tar –xvf xxx.tar
	tar -xvf xxx.tar.gz -C /usr/aaa

	

### 常见问题

1、虚拟机中linux系统ifconfig的ip地址为127.0.0.1

解决方案：

思路主要有3点：

1）重命名 ifcfg-eth0 为 ifcfg-eth1 或者其他数字

2）更改DEVICE

3）更改HWADDR

[解决linux网络驱动问题](https://www.linuxidc.com/Linux/2012-12/76248.htm)