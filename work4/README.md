# README

## 项目简介

基于SpringBoot框架构建的弹幕视频网站的API接口。包含用户、视频、互动、社交等模块。



## 接口文档

https://apifox.com/apidoc/shared-39b3b10a-35de-4f17-87d9-701f346e587e



## 技术栈

| 技术         |
| ------------ |
| SpringBoot   |
| MyBatis      |
| MyBatis-Plus |
| JJWT         |
| Lombok       |
| Redis        |
| Docker       |





## 项目功能

- 接口

| 模块名 | 最低需要完成的接口                                         | 数量 |
| ------ | ---------------------------------------------------------- | ---- |
| 用户   | 注册、登录、用户信息、上传头像                             | 4    |
| 视频   | 投稿、发布列表、搜索视频、热门排行榜                       | 4    |
| 互动   | 点赞操作、点赞列表、评论评论、评论视频、评论列表、删除评论 | 5    |
| 社交   | 关注操作、关注列表、粉丝列表、好友列表                     | 4    |

- 使用security框架实现了用户登录
- 通过定时任务和redis，每隔一小时更新热门排行榜
- 对上传视频进行异步处理



## 项目结构图

├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─clipg
│  │  │          ├─config
│  │  │          ├─controller
│  │  │          ├─dto
│  │  │          ├─entity
│  │  │          ├─exception
│  │  │          ├─filter
│  │  │          ├─mapper
│  │  │          ├─service
│  │  │          │  └─impl
│  │  │          ├─task
│  │  │          └─util
│  │  └─resources
│  │      └─META-INF
│  └─test
│      └─java
│          └─com
│              └─clipg
├─Dockerfile
└─pom.xml

