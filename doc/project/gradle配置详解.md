## gradle ##

### 什么是gradle？ ###

Gradle是一个基于Apache Ant和Apache Maven概念的项目自动化建构工具。它使用一种基于`Groovy`的特定领域语言来声明项目设置，而不是传统的XML。当前其支持的语言限于Java、Groovy和Scala，计划未来将支持更多的语言。

### gradle配置详解 ###

- gradle-wrapper.properties

gradle-wrapper.properties的目录是在gradle/wrapper目录下，gradle-wrapper.properties的配置主要有以下几项
	
	# 下载的Gradle压缩包解压后存储的主目录  GRADLE_USER_HOME为环境变量
    distributionBase=GRADLE_USER_HOME
	# 相对于distributionBase的解压后Gradle压缩包的路径
    distributionPath=wrapper/dists
	# 同distributionBase，不过是存放zip压缩包的
    zipStoreBase=GRADLE_USER_HOME
	# 同distributionPath，不过是存放zip压缩包的
    zipStorePath=wrapper/dists
	# Gradle发行版压缩包的下载地址
    distributionUrl=https\://services.gradle.org/distributions/gradle-5.2.1-all.zip

- settings.gradle
	
- build.gradle

