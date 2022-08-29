<div align="center">
    <img src="http://gchat.qpic.cn/gchatpic_new/2094085327/695525945-2916960648-F41E176B39C491DECA3AA5D2373FCE43/0?term&#61;3" alt="logo" style="width:233px ;height:233px;border-radius:50%"/>
    <p>
    	<h2>
        	无量姬的Q群机器人
    	</h2>
</div>

该项目为Springboot项目，此项目基于[simple bot v2]

## 项目文档
暂时没有

## 项目地址
WuLiang-Bot
[GitHub](https://github.com/2094085327/WuLiang-Bot)

## 运行环境

`maven` `Java1.8+` `Mysql 8.0 `

## 你需要掌握的知识
1.能够主观的，能动的，熟练的运用[`百度`](https://www.baidu.com)进行查询

2.对`Springboot`有基础的了解,建议先掌握`Spring`

3.对`Mybatis-Plus`有一定了解，建议先掌握`Mybatis`

# 如何开始?
1.打开`src/main/resources/simbot-bots`文件夹, 在里面创建一个`*.bot`文件, `*`可以是任意字符, [参考](https://www.yuque.com/simpler-robot/simpler-robot-doc/fk6o3e#iUKbX)

2.打开`\src\main\resources\`文件夹, 在里面创建一个`application-*.yml`文件, 如果你会`springboot`, 应该能看懂这一步, 下面是本项目所需要的配置项
```yaml
#配置数据源
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/genshin?useSSL=FALSE&serverTimezone=UTC
    username: root
    password: 
    type: com.alibaba.druid.pool.DruidDataSource 
```