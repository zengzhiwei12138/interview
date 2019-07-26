## zookeeper_基本篇

### api
	# 创建节点  创建一个名为/path的节点 数据为data
	create /path data
	# 删除节点
	delete /path
	# 节点是否存在
	exists /path
	# 修改节点
    setData /path data
	# 获取节点数据
	getData /path
	# 获取子节点 返回/path下所有子节点
	getChildren /path


### znode类型
- 持久节点 persistent
- 临时节点 ephemeral 会话结束就删除   临时节点不能有子节点
- 持久有序节点 persistent_sequential
- 临时有序节点 ephmeral_sequential