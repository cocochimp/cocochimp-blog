# 快速开始

>运行方式

1. 启动项目：

* redis：运行项目时打开redis-server.exe

* nacos：运行项目时打开nacos文件夹下的bin下的startup.cmd

2. sg-vue-admin【后端管理系统】
3. sg-blog-vue【博客页面】

* 创建：npm install
* 启动：npm run dev
* 打包：npm run build



# 运行环境

- java:8（jdk1.8）
- mysql:5.6.35
- redis:3.0
- nacos:1.1.3



# 核心依赖

| 依赖         | 版本    |
| ------------ | ------- |
| Spring Boot  | 2.5.0   |
| Mybatis Plus | 3.4.3   |
| aliyun oss   | 3.10.2  |
| swagger      | 2.9.2   |
| fastjson     | 1.2.33  |
| easyexcel    | 2.1.1   |
| jjwt         | 0.9.0   |
| lombok       | 1.18.24 |



# blog页面预览

### 首页

* 地址：http://localhost:8080/#/

![image-20230420093738070](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420093738.png)

![image-20230420093915518](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420093915.png)



### 文章详情页面

* http://localhost:8080/#/DetailArticle?aid=10

![image-20230420094020987](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420094021.png)



### 友链信息

* 地址：http://localhost:8080/#/Friendslink

![image-20230420094142399](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420094142.png)



### 分类页表

* 地址：http://localhost:8080/#/Share?classId=1

![image-20230904171546343](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904171553.png)



# admin页面预览

## 登录页

* 地址：http://localhost:81/#/dashboard

![image-20230420094542851](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420094543.png)

| 用户名 | 密码 | 权限 |
| ------ | ---- | ---- |
| admin  | 1234 | all  |

* 项目功能预览

![image-20230420094811762](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420094811.png)

![image-20230420094834867](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420094834.png)



## 写博文

* 地址：http://localhost:81/#/write

![image-20230420095506594](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420095506.png)



## 系统管理【*】

### 用户管理

* 地址：http://localhost:81/#/system/user

![image-20230904172116020](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904172116.png)

* 功能：管理员管理登录账户信息（CRUD）



### 角色管理

* 地址：http://localhost:81/#/system/role

![image-20230904172149892](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904172149.png)

* 功能：对角色身份信息进行管理



### 菜单管理

* 地址：http://localhost:81/#/system/menu

![image-20230420094910461](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230420094910.png)

* 功能：管理左侧导航栏展示菜单信息



## 内容管理【*】

### 文章管理

* 地址：http://localhost:81/#/content/article

![image-20230904172452053](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904172452.png)

* 功能：对所有文章进行管理



### 分类管理

* 地址：http://localhost:81/#/content/category

![image-20230904172742018](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904172742.png)

* 功能：对分类进行管理



### 友链管理

* 地址：http://localhost:81/#/content/link

![image-20230904172806139](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904172806.png)

* 功能：对友链进行管理



### 标签管理

* 地址：http://localhost:81/#/content/tag

![image-20230904172926364](https://cocochimp-img.oss-cn-beijing.aliyuncs.com/23-03/20230904172926.png)

* 功能：对文章标签进行管理



# 待改进内容

> 1、前端

1. 页面问题

- [ ] 代办页面

2. 功能问题

- [ ] 播放“播放音乐”功能
- [ ] 增加“搜索”功能ES



> 2、后端

- [ ] Redis数据变化后无法写入数据库
- [ ] 图片回显慢的问题：https://www.zhihu.com/question/289587570
