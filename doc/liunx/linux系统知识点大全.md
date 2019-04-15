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
	
	
