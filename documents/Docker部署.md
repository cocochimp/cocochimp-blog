# 1、项目部署规划
云服务器配置：Centos7.8（Centos8以上有部分命令不相同，可以自行上网查询）

1. **各模块运行端口：**
- 前台服务模块cocochimp-blog->7777
- 后台服务模块cocochimp-admin->8989
- 前端前台Vue项目：cocochimpblog-vue->8093
- 前端后台Vue项目：cocochimpblog-vue-admin->8094
2. **docker所需镜像版本：**
- java:8（jdk1.8）
- mysql:5.6.35
- redis:3.0
- nginx:1.18.0
- node:16.18.1
3. **部署步骤：**
- 安装docker
- 拉取java:8镜像，后端项目使用maven打包，上传jar包到服务器指定目录/mydata，编写**Dockerfile文件**，将后端项目打成镜像文件。
- 拉取mysql:5.6.35、redis:3.0、nginx:1.18.0、node:16.18.1镜像。
- 编写docker-compose.yml文件，使用**docker-compose容器编排管理**容器运行。
- 配置环境：
   - 配置mysql，导入sql文件
   - 配置redis，修改redis.conf文件
   - 配置nginx，将打好包的Vue项目放到html目录下，并配置nginx.conf文件
- 测试运行
- 镜像上传阿里云镜像仓库

# 2、准备工作
## 2.1 后端项目

1. **修改后端项目的application.yaml文件**
- 将MySQL服务和Redis服务的localhost修改为服务器ip

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693364269290-a3be2093-9542-4a68-bf20-085ef01ba27a.png#averageHue=%234d6845&clientId=ub48347ea-cf88-4&from=paste&height=357&id=u9f788e6a&originHeight=446&originWidth=1084&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=81327&status=done&style=none&taskId=u1d0485e2-4a37-4ab8-9729-0c9ca33a19d&title=&width=867.2)

2. **后端项目使用maven打包**

将子模块cocochimp-blog和cocochimp-admin与父模块cocochimp-framework关联

- 父模块cocochimp-framework
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.10.1</version>
        </plugin>
        <!-- 此插件必须放在父 POM 中  -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.3.0</version>
            <executions>
                <!--
                执行本插件的方法为，在主目录下执行如下命令：
                mvn package assembly:single

                对于 IntelliJ IDEA，生成的 JAR 包位于每个模块下的文件夹 target
                -->
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals>
                        <!-- 此处 IntelliJ IDEA 可能会报红，这是正常现象  -->
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <archive>
                    <manifest>
                        <!-- 配置程序运行入口所在的类 -->
                        <!-- 自己的启动类path-->
                        <mainClass>com.cocochimp.cocochimpAdminApplication</mainClass>
                    </manifest>
                    <manifest>
                        <!-- 配置程序运行入口所在的类 -->
                        <mainClass>com.cocochimp.cocochimpBlogApplication</mainClass>
                    </manifest>
                </archive>
                <!-- 设置 JAR 包输出目录 -->
                <outputDirectory>${project.build.directory}/#maven-assembly-plugin</outputDirectory>
                <!-- 设置打包后的 JAR 包的目录结构为默认 -->
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- 子模块cocochimp-blog和cocochimp-admin
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <fork>true</fork> <!-- 如果没有该配置，devtools不会生效 -->
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
    <finalName>${project.artifactId}</finalName>
</build>
```

3. **打包**

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693364539700-6682c138-4155-4b5e-82f2-61fddd3aec74.png#averageHue=%233a4044&clientId=ub48347ea-cf88-4&from=paste&height=245&id=ua9be4e34&originHeight=306&originWidth=361&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=19991&status=done&style=none&taskId=u5588d526-705b-4327-9d59-db5f84345e8&title=&width=288.8)

- 子模块jar包—>注意看文件大小，如果是几十k的话说明没有绑定到父模块

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693364607060-c8fb1706-ba8c-4855-a8a7-725a78cc048d.png#averageHue=%23959681&clientId=ub48347ea-cf88-4&from=paste&height=423&id=u3bf5d8cc&originHeight=529&originWidth=506&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=31607&status=done&style=none&taskId=uf7cb5609-0e4a-44b2-8b9e-68c3334c933&title=&width=404.8)

4. **测试后端jar包项目**
```xml
java -jar cocochimp-admin.jar
java -jar cocochimp-blog.jar
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693364797579-673f0787-736f-49e9-bb83-da795d95ad0d.png#averageHue=%237a7775&clientId=ub48347ea-cf88-4&from=paste&height=514&id=u036e8b19&originHeight=643&originWidth=1013&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=86610&status=done&style=none&taskId=u873a7842-d704-41b1-935d-bdae3f01cc3&title=&width=810.4)

## 2.2 前端项目

1. **修改Vue项目的运行端口：**
- 前端Vue项目：8093
   - index.js文件

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693364923579-99ebd54b-fa5e-4894-8be3-fda027624f7d.png#averageHue=%23261e1c&clientId=ub48347ea-cf88-4&from=paste&height=164&id=u7d56af83&originHeight=205&originWidth=669&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=28629&status=done&style=none&taskId=u99b91fdf-1cf5-43c4-81e4-b850a017db0&title=&width=535.2)

- 后端Vue项目：8094
   - vue.config.js文件

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365481056-57b13751-a6a2-4eb1-b635-df3d2b16c0cf.png#averageHue=%2322201f&clientId=ub48347ea-cf88-4&from=paste&height=85&id=u4a7342d3&originHeight=106&originWidth=804&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=15506&status=done&style=none&taskId=ue382e525-b550-429f-8fd0-371db249900&title=&width=643.2)

2. **修改前端对应的服务器ip：**
- 前端Vue项目：8093—>7777

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365083774-b45dc4ba-ea8b-4de3-a8c5-ac5ec3bb561c.png#averageHue=%23211e1d&clientId=ub48347ea-cf88-4&from=paste&height=331&id=a5Pfu&originHeight=414&originWidth=981&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=70656&status=done&style=none&taskId=u6b996648-1148-48db-a094-59c192c15ba&title=&width=784.8)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365158729-77cde27e-0b1f-40cd-b9b8-de2924c4fd7b.png#averageHue=%23211e1d&clientId=ub48347ea-cf88-4&from=paste&height=260&id=ua3c4e274&originHeight=325&originWidth=1088&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=41395&status=done&style=none&taskId=u4b3182e5-3aa0-4171-b8b1-660a80f8732&title=&width=870.4)

- 后端Vue项目：8094—>8989

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365564438-2aec996b-4bd2-4c6c-940b-97cb3640fa5d.png#averageHue=%23211f1f&clientId=ub48347ea-cf88-4&from=paste&height=179&id=uc6755869&originHeight=224&originWidth=1020&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=37600&status=done&style=none&taskId=u72482961-d252-4d2c-a6cb-ab3c42c37d4&title=&width=816)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365611978-fb674d14-bbba-4fc0-b982-9125fffb1e5e.png#averageHue=%23251e1d&clientId=ub48347ea-cf88-4&from=paste&height=158&id=u348a7539&originHeight=198&originWidth=881&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=19414&status=done&style=none&taskId=u64aed657-057c-48f4-bb12-ba362f200af&title=&width=704.8)

3. **项目打包：**
- 前端Vue项目：8093
```xml
npm run build
```

- 后端Vue项目：8094
```xml
npm run build:prod
```

   - 如果你打包的过程中出现以下错误，表示 node.js版本过高，可以关闭node.js安全校验后再打包
```xml
set NODE_OPTIONS=--openssl-legacy-provider
```
![image-20221121084043547.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365738651-abb3643e-0148-49f8-bfb0-635602be56ce.png#averageHue=%232c2c2b&clientId=ub48347ea-cf88-4&from=drop&id=u7ee98e75&originHeight=227&originWidth=1226&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=39510&status=done&style=none&taskId=u3a3c872d-9c25-4040-ab71-77e88285421&title=)

   - 如果使用出现ERROR，请注释掉以下内容

![image-20221121091856207.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693365751054-0ffd3b6d-ff5d-4a99-81e9-6743d6348c2f.png#averageHue=%237a6f50&clientId=ub48347ea-cf88-4&from=drop&id=u721dc385&originHeight=253&originWidth=1118&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=43853&status=done&style=none&taskId=u8153184f-deed-4385-b754-2fa91409aa2&title=)

## 2.3 服务器配置

1. 新建open_ports.sh脚本打开所有需要运行服务的端口
```shell
# 新建test.sh脚本
touch open_ports.sh
vim open_ports.sh

# 脚本内容
  #!/bin/bash
  # 定义需要开启的端口
  ports=("80" "7777" "8989" "6379" "3306" "8093" "8094")
  # 遍历端口列表
  for port in ${ports[@]}; do
      # 检查端口是否已开启
      firewall-cmd --list-ports | grep -wq $port
      if [ $? -eq 0 ]; then
          echo "端口 $port 已开启，跳过..."
          continue
      fi
      # 开启端口
      firewall-cmd --zone=public --add-port=$port/tcp --permanent
      # 重新加载防火墙规则
      firewall-cmd --reload
      echo "端口 $port 开启成功"
  done

# 运行脚本
bash open_ports.sh
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693368040614-66d462e7-1bed-4adf-8522-a1d874dfee70.png#averageHue=%230c0a07&clientId=ub48347ea-cf88-4&from=paste&height=123&id=u0f986057&originHeight=154&originWidth=425&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=8022&status=done&style=none&taskId=u55b4b0bc-7a76-45f1-a5a8-69cbbd9f4cf&title=&width=340)

2. 重启防火墙
```shell
systemctl restart firewalld.service
```

3. **查看已经开放的端口**
```shell
# 查看已经开放的端口
firewall-cmd --permanent --zone=public --list-ports
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693368055448-41470e78-1c98-40c0-b44e-096952db6313.png#averageHue=%230e0a07&clientId=ub48347ea-cf88-4&from=paste&height=54&id=u598ccb1f&originHeight=68&originWidth=715&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=6962&status=done&style=none&taskId=u1ce044e8-1b9b-4114-9151-51af49142c8&title=&width=572)

4. **配置安全组（开放端口）**

这里使用的是华为云HECS，添加的是入方向规则
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693368387464-696e8aac-ee1b-445e-babc-a05ed3215f46.png#averageHue=%23eff1c8&clientId=ub48347ea-cf88-4&from=paste&height=42&id=u26d8d08e&originHeight=92&originWidth=1390&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=14351&status=done&style=none&taskId=ube335295-5b63-4b1c-bc9a-6b821872ee1&title=&width=639)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693368404890-b5c4f84b-0aaa-40b5-937b-b1666053424a.png#averageHue=%23fcfcfb&clientId=ub48347ea-cf88-4&from=paste&height=141&id=ub50f9cda&originHeight=305&originWidth=1377&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=40667&status=done&style=none&taskId=u1970e9a3-795f-492e-bc84-46bf871f974&title=&width=638)

# 3、Docker安装
> 1、Docker环境准备

- Linux要求内核3.0以上
```shell
# 服务器内核系统信息
cat /etc/os-release
# 服务器内核版本
uname -r    
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693368544231-6cb682d1-d9ca-4247-93bd-488d2324b444.png#averageHue=%23120e0a&clientId=ub48347ea-cf88-4&from=paste&height=60&id=u9a0ef3e0&originHeight=75&originWidth=462&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=7641&status=done&style=none&taskId=ud1490d46-5165-4ee8-98f6-82d32894523&title=&width=369.6)

> 2、Docker安装

1. yum安装gcc相关环境（需要确保虚拟机可以上外网）
```shell
yum -y install gcc
yum -y install gcc-c++
```

2. 卸载旧版本
```shell
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

3. 安装需要安装的软件包
```shell
yum install -y yum-utils
```

4. 设置镜像的仓库
```shell
# 推荐使用国内aliyun镜像仓库
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

5. 更新yum软件包索引
```
yum makecache fast
```

6. 安装docker引擎：docker-ce
```bash
# docker-ce 社区版 而ee是企业版
yum install docker-ce docker-ce-cli containerd.io
```

7. 启动docker
```shell
#1、CentOS7
systemctl start docker

#2、CentOS8
systemctl restart docker.service
```

8. 测试命令
```shell
#使用docker version查看是否按照成功
docker version

#测试
docker run hello-world

#查看一下下载的镜像
➜ docker images         
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    feb5d9fea6a5   7 months ago   13.3kB
```

- 需要创建一个daemon.json文件
```shell
# 进入/etc/docker，没有daemon.json文件就自己创一个
cd /etc/docker

# 编辑daemon.json
{
 "registry-mirrors": ["你个人的阿里云镜像加速器地址"]
}
```

9. 重启docker服务
```shell
systemctl daemon-reload
systemctl restart docker.service
```
![image-20221122110013829.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693371951918-1f5692f5-8332-4f89-aef4-9d08f2c9cf87.png#averageHue=%23fbf9f8&clientId=ub48347ea-cf88-4&from=drop&id=u6cedea7d&originHeight=288&originWidth=833&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=79946&status=done&style=none&taskId=ua4cb8d4a-afc4-42bd-8f50-2e73759477d&title=)

> 其他镜像命令：

- 删除指定镜像
```shell
docker rmi <镜像名称或容器ID>
# 例：
docker rmi springboot:latest ## docker rmi 指定名称
docker rmi ed603a4c67bb ## docker rmi 指定镜像id
```

- 查看容器和镜像
```shell
docker ps -a ## 查看运行的容器【-a 运行的容器信息】
docker images ## 查看镜像
```

# 4、拉取镜像

- 拉取的镜像：
```shell
# 拉取镜像java:8(jdk1.8)
docker pull java:8

# 拉取 Mysql:5.6.35 镜像
docker pull mysql:5.6.35

# 拉取镜像redis:3.0
docker pull redis:3.0

# 拉取镜像nginx:1.18.0
docker pull nginx:1.18.0

# 拉取镜像node:16.18.1
docker pull node:16.18.1
```
查看当前所有镜像：docker images
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693373275803-19193db7-906c-464d-9ea3-96daf9feccae.png#averageHue=%230e0806&clientId=ub48347ea-cf88-4&from=paste&height=169&id=uf40f09ca&originHeight=211&originWidth=988&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=43563&status=done&style=none&taskId=udaa95491-9617-48f5-ba2e-e528290f44a&title=&width=790.4)

# 5、Dockerfile构建个人镜像

1. 首先将jar包传到服务器指定目录下
- 我这里是在根目录下创建了一个mydata目录

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693372759745-60fab3da-5d86-4d4b-b311-abf9a7d60c82.png#averageHue=%231b0c09&clientId=ub48347ea-cf88-4&from=paste&height=37&id=ud92a0a3e&originHeight=46&originWidth=362&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=3571&status=done&style=none&taskId=u90faaa33-a0e9-4845-a482-fe94c3e9be1&title=&width=289.6)

2. 在mydata目录下创建Dockerfile文件，编写Dockerfile文件
- 这里我们两个jar包分别打成两个镜像，因为一个目录下只能有一个Dockerfile文件，所以当构建完第一个镜像之后修改对应的Dockerfile文件。
- cocochimp-admin.jar的Dockerfile文件
```shell
## 第一个dockerfile文件
FROM java:8
MAINTAINER cocochimp
VOLUME /tmp
ADD cocochimp-blog.jar cocochimp_blog.jar
RUN bash -c 'touch /cocochimp_blog.jar'
ENTRYPOINT ["java","-jar","/cocochimp_blog.jar"]
EXPOSE 7777

## 创建dockerimages
docker build -t cocochimp_blog:1.0 .
```

- cocochimp-blog.jar的Dockerfile文件
```shell
## 第二个dockerfile文件
FROM java:8
MAINTAINER cocochimp
VOLUME /tmp
ADD cocochimp-admin.jar cocochimp_admin.jar
RUN bash -c 'touch /cocochimp_admin.jar'
ENTRYPOINT ["java","-jar","/cocochimp_admin.jar"]
EXPOSE 8989

## 创建dockerimages
docker build -t cocochimp_admin:1.0 .
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693373195126-270bf20c-f4e4-48aa-94f8-ccce643ed54b.png#averageHue=%230d0907&clientId=ub48347ea-cf88-4&from=paste&height=96&id=ubd7874f8&originHeight=120&originWidth=976&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=27169&status=done&style=none&taskId=u6dc2e889-31c6-4876-8df8-a926fa6353a&title=&width=780.8)

# 6、Docker-compose容器编排
## 6.1 安装Docker-compose
如果是低版本的compose，docker和compose之间要有-

1. 首先查看主机是否有docker-compose环境
```shell
docker compose version
```
没有就安装：由于docker-compose是从github上安装的，有可能是安装失败，所以需要那啥，dddd
```shell
DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker} 
mkdir -p $DOCKER_CONFIG/cli-plugins 
curl -SL https://github.com/docker/compose/releases/download/v2.11.2/docker-compose-linux-x86_64 -o $DOCKER_CONFIG/cli-plugins/docker-compose
chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693373158796-bf2b5c6f-3e97-4e81-b490-2906484857ab.png#averageHue=%230f0c08&clientId=ub48347ea-cf88-4&from=paste&height=54&id=u8435f864&originHeight=67&originWidth=456&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=5983&status=done&style=none&taskId=ue9aab8b0-b11d-40b7-a444-21a74509c58&title=&width=364.8)

## 6.2 配置nginx文件

1. 在/目录下创建一个app目录，进入app目录
```shell
mkdir /app
```

2. 首先创建一个nginx容器，只是为了复制出配置
```shell
# 1.运行容器
docker run -p 80:80 --name nginx -d nginx:1.18.0

# 2.将容器内的配置文件拷贝到当前目录/app中:
docker container cp nginx:/etc/nginx .
# 3.将文件nginx修改为conf
mv nginx conf
# 4.创建文件夹nginx
mkdir nginx
# 5.将conf目录拷贝到nginx目录
cp -r conf nginx/
# 6.删除conf目录
rm -rf conf

# 7.停止并删除容器
docker stop nginx && docker rm nginx 
```

## 6.3 编写docker-compose.yml文件

1. 在/mydata目录下创建docker-compose.yml文件
```yaml
#compose版本
version: "3"  
 
services:
  cocochimp_blog:
    #微服务镜像  
    image: cocochimp_blog:1.0
    container_name: cocochimp_blog
    ports:
      - "7777:7777"
    #数据卷
    volumes:
      - /app/cocochimp_blog:/data/cocochimp_blog
    networks: 
      - blog_network
    depends_on: 
      - redis
      - mysql
      - nginx

  cocochimp_admin:
    #微服务镜像
    image: cocochimp_admin:1.0
    container_name: cocochimp_admin
    ports:
      - "8989:8989"
    #数据卷
    volumes:
      - /app/cocochimp_admin:/data/cocochimp_admin
    networks:
      - blog_network
    depends_on:
      - redis
      - mysql
      - nginx
     
  #redis服务
  redis:
    image: redis:3.0
    ports:
      - "6379:6379"
    volumes:
      - /app/redis/redis.conf:/etc/redis/redis.conf
      - /app/redis/data:/data
    networks: 
      - blog_network
    command: redis-server /etc/redis/redis.conf

  #mysql服务
  mysql:
    image: mysql:5.6.35
    environment:
      MYSQL_ROOT_PASSWORD: '123456'
      MYSQL_ALLOW_EMPTY_PASSWORD: 'no'
      MYSQL_DATABASE: 'cocochimpBlog'
      MYSQL_USER: 'root'
      MYSQL_PASSWORD: '123456'
    ports:
       - "3306:3306"
    volumes:
       - /app/mysql/db:/var/lib/mysql
       - /app/mysql/conf/my.cnf:/etc/my.cnf
       - /app/mysql/init:/docker-entrypoint-initdb.d
    networks:
      - blog_network
    #解决外部无法访问
    command: --default-authentication-plugin=mysql_native_password 

  #nginx服务
  nginx:
    image: nginx:1.18.0
    ports:
      - "80:80"
      - "8093:8093"
      - "8094:8094"
    volumes:
      - /app/nginx/html:/usr/share/nginx/html
      - /app/nginx/logs:/var/log/nginx
      - /app/nginx/conf:/etc/nginx
    networks:
      - blog_network
    
#创建自定义网络
networks: 
   blog_network: 
```

2. 检查当前目录下compose.yml文件是否有语法错误
```shell
docker compose config -q
```

3. 启动所有docker-compose服务并后台运行
```shell
docker compose up -d
```
查看正在运行的容器实例
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693373599081-e350456c-c2e8-4588-b3af-5996741d3d96.png#averageHue=%23090604&clientId=ub48347ea-cf88-4&from=paste&height=200&id=u83f4097c&originHeight=250&originWidth=1184&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=29145&status=done&style=none&taskId=uc0683d74-71fb-4b81-89b3-0f0fe24d599&title=&width=947.2)
注意：因为MySQL和Redis还未配置，会出现容器挂掉的情况，这点等待配置过MySql和Redis之后再重启容器实例即可

# 7、配置容器环境
## 7.1 配置MySQL

1. 将sql文件传到/app/mysql/db目录下(==和MySQL容器实例相同的容器数据卷位置==)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693373768297-42fdd51a-f87a-48ad-9aa8-4cfdc0291554.png#averageHue=%230a0705&clientId=ub48347ea-cf88-4&from=paste&height=79&id=uab561af8&originHeight=99&originWidth=611&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=10644&status=done&style=none&taskId=u5a5a18b3-8e7b-4526-8f36-f19e104ac53&title=&width=488.8)

2. 进入MySQL容器实例
```shell
docker exec -it 容器ID bash
mysql -uroot -p
```

3. 登录MySQL
-  密码就是在docker-compose.yml文件当中进行配置

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693374138138-c9efa977-42c9-42da-84de-e11291f90d79.png#averageHue=%23080503&clientId=ub48347ea-cf88-4&from=paste&height=250&id=u937b10b1&originHeight=313&originWidth=725&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=24501&status=done&style=none&taskId=u81267ff2-2259-4d84-b54a-761e355358c&title=&width=580)
```shell
create database cocochimpBlog; # 创建数据库
use cocochimpBlog; # 进入数据库
source /var/lib/mysql/cocochimpBlog.sql; # 执行sql文件 
show tables; # 展示数据库的表
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693374246826-49ccbd30-25e5-463c-9c67-989e17def7bc.png#averageHue=%23070503&clientId=ub48347ea-cf88-4&from=paste&height=263&id=u891fd440&originHeight=329&originWidth=306&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=9589&status=done&style=none&taskId=u05a4bbc6-73ad-4ca6-a6a5-5ed0b9489a6&title=&width=244.8)

## 7.2 配置Redis

1. **获取redis对应版本的配置文件**

因为docker拉取的redis镜像是没有redis.conf文件的。因此，就需要我们官网上找一个相对应版本的的**redis.conf配置文件**
redis配置文件官网[Redis configuration | Redis](https://redis.io/docs/manual/config/)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693374466998-0fabc99d-de12-41b0-9420-09194babdecc.png#averageHue=%23fefbfb&clientId=ub48347ea-cf88-4&from=paste&height=293&id=u084d0e19&originHeight=366&originWidth=713&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=62741&status=done&style=none&taskId=u054b2a7b-3e88-40e7-9a93-43ed7b2943b&title=&width=570.4)

2. **进入/app/redis目录下**
- 不知为何docker-compose.yml文件写的没有问题，但是这里创建的redis.conf文件却是一个文件夹，无奈只好删除redis.conf目录，创建一个redis.conf文件

【问题很大！可能会出现容器挂掉的问题，解决方案：关闭并删掉所有docker正在运行的容器，重新用‘docker compose up -d’编排启动服务】

- 使用vim命令进入vim编辑器，将redis配置文件内容粘贴进去
```shell
touch redis.conf # 创建redis.conf文件
vim redis.conf # 编辑文件
```

3. **修改配置文件内容 —> 使用/进行搜索**
- 添加redis密码（requirepass）

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693380324189-90a490c0-0503-4633-8b8c-1c78ced85952.png#averageHue=%23eeebe8&clientId=ub48347ea-cf88-4&from=paste&height=87&id=uc3a9c260&originHeight=109&originWidth=263&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=7116&status=done&style=none&taskId=u94225077-4a11-40f2-a06c-2628409ffa8&title=&width=210.4)

- 修改bind为0.0.0.0（任何机器都能够访问）

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693380341692-00e21d4f-8512-4c54-8625-c90e81fe94ce.png#averageHue=%23f7f5f4&clientId=ub48347ea-cf88-4&from=paste&height=62&id=u9d3cbbd3&originHeight=77&originWidth=359&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=3682&status=done&style=none&taskId=u6dbbe265-354d-48ba-9ab6-f9757bcf834&title=&width=287.2)

- 将后台启动设置为no（daemonize no）：为了避免和docker中的-d参数冲突

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693380382766-8039a831-4bb7-4af6-971b-c53ff6664bc6.png#averageHue=%23cca866&clientId=ub48347ea-cf88-4&from=paste&height=78&id=uc45b7c6a&originHeight=98&originWidth=420&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=9537&status=done&style=none&taskId=u914fc8ae-5ec9-435b-a79c-65bd4848d88&title=&width=336)

- 关闭保护模式(protected-mode no) —> 有则修改

![image-20221120135253947.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693380409813-8759cdda-aa13-4174-9bac-93ebeae7ce29.png#averageHue=%23ebe9da&clientId=ub48347ea-cf88-4&from=drop&id=u9e43ef6f&originHeight=72&originWidth=231&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=4039&status=done&style=none&taskId=u0816c542-fb70-4cd6-b005-cd1ec24b2a0&title=)

## 7.3 测试后端接口

1. 重启MySQL和Redis服务
```yaml
docker restart 容器ID
```

2. 使用postman进行接口测试—>测试通过

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693381977155-468f3b52-8711-4096-a756-f46fe1596af1.png#averageHue=%23f8f7f6&clientId=ub48347ea-cf88-4&from=paste&height=364&id=ue7b1a967&originHeight=455&originWidth=946&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=76920&status=done&style=none&taskId=u00c4efbd-8ef8-451f-bd68-44049337af7&title=&width=756.8)

3. 将已经打包好的前端项目放到本地Nginx里面测试

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382126009-6885b90b-d2df-455b-9c60-4895580e80be.png#averageHue=%23faf9f8&clientId=ub48347ea-cf88-4&from=paste&height=231&id=u66237164&originHeight=289&originWidth=645&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=24066&status=done&style=none&taskId=u9031f8e7-b3cd-4c28-84d8-f2a915dd357&title=&width=516)

4. 将dist文件直接放到html目录下

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382145111-9bc24862-9afd-4b78-a384-35f40648ab65.png#averageHue=%23fbfaf9&clientId=ub48347ea-cf88-4&from=paste&height=130&id=u8a79bc1d&originHeight=163&originWidth=330&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=5920&status=done&style=none&taskId=u04c07ca4-201f-4f41-a294-69b55b938b3&title=&width=264)

5. 在conf文件中修改nginx.conf文件，在http{......}中添加内容如下
```yaml
server {
    listen       8093;
    server_name  localhost;
    location / {
        root   html/blog_dist;
        index  index.html index.htm;
    }
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   html;
    }
}
server {
    listen       8094;
    server_name  localhost;
    location / {
        #dist文件所在目录
      root   html/admin_dist;
      index  index.html index.htm;
        # 解决刷新404的问题
      try_files $uri $uri/ /index.html;
    }
}
```

6. 启动nginx.exe文件运行nginx服务
- 访问localhost:8094

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382305226-de13519e-b84e-45b3-b83b-404ca73be595.png#averageHue=%23debb7f&clientId=ub48347ea-cf88-4&from=paste&height=224&id=ua4aa0f95&originHeight=280&originWidth=794&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=26609&status=done&style=none&taskId=u6ed54bc8-2455-4202-8b5d-74645426b66&title=&width=635.2)

## 7.4 配置Nginx

1. 将打包好后的两个dist文件夹重命名发送到/app/nginx/html目录下

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382385352-4334ff9f-4c6b-4e43-bd93-9492ee0de4eb.png#averageHue=%230c0906&clientId=ub48347ea-cf88-4&from=paste&height=70&id=u60fbb9bd&originHeight=88&originWidth=423&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=6494&status=done&style=none&taskId=ua74da174-e098-4254-9b43-9263839616f&title=&width=338.4)

2. 修改nginx.conf配置文件

进入conf文件夹下，打开nginx.conf文件，在http{.......}添加两个server
```yaml
#博客前端vue
server {
  listen       8093;
  server_name  localhost;

  location / {
     root   /usr/share/nginx/html/blog_dist;
     index  index.html index.htm;
     try_files $uri $uri/ /index.html;
     }
}

#博客后端vue
server {
  listen       8094;
  server_name  localhost;

  location / {
     root   /usr/share/nginx/html/admin_dist;
     index  index.html index.htm;
     try_files $uri $uri/ /index.html;
     }
}
```

- 记得root路径为容器内的dist文件路径

# 8、项目测试
访问对应ip和端口，查看项目是否有问题

- **前端：**

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382536992-bb30133e-3b30-4d00-ae6d-35d195bd03b8.png#averageHue=%2395c3ad&clientId=ub48347ea-cf88-4&from=paste&height=751&id=uc66d9c13&originHeight=939&originWidth=1915&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=2085008&status=done&style=none&taskId=u4d682664-41fb-47be-8dc3-5c0adc40a5e&title=&width=1532)

- **后台：**

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382569322-949abe22-256a-4180-86f1-d1bf96f77414.png#averageHue=%23d6a868&clientId=ub48347ea-cf88-4&from=paste&height=581&id=u29b8f8f4&originHeight=726&originWidth=1918&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=78069&status=done&style=none&taskId=u41984b2b-8227-4975-bb39-cfe9e23b726&title=&width=1534.4)

# 9、将镜像发布到阿里云镜像仓库
这里主要是将两个后端子模块镜像上传到阿里云镜像仓库

1. 在阿里云中点击镜像容器服务，创建个人实例，再点击创建命名空间

![image-20221122093612920.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382626456-c19c9857-068c-45e2-9e20-ad530445fdae.png#averageHue=%23fafafa&clientId=ub48347ea-cf88-4&from=drop&height=290&id=u8b75a6a7&originHeight=411&originWidth=665&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=21516&status=done&style=none&taskId=u086e2302-7b0a-408e-ad23-3c833c8142e&title=&width=470)

2. 点击镜像仓库，创建镜像仓库

![image-20221122093825428.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382644845-3fae1092-d7a9-406d-8447-bcd406bcbe5a.png#averageHue=%23fdfdfd&clientId=ub48347ea-cf88-4&from=drop&height=387&id=uf3545e4d&originHeight=819&originWidth=966&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=34557&status=done&style=none&taskId=u991fa956-6046-4217-97b1-aec5ed02e2b&title=&width=457)
![image-20221122093919985.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382653186-3d461123-e46c-4664-b425-98774f63b48a.png#averageHue=%23fcfcfc&clientId=ub48347ea-cf88-4&from=drop&height=193&id=u68211a63&originHeight=404&originWidth=956&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=22779&status=done&style=none&taskId=u1f3c6d79-9b9e-4c03-970a-266788481b7&title=&width=456)

3. 查看操作镜像仓库操作指南

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382738742-09b90e86-f1e3-4179-a765-1d82dee15e8f.png#averageHue=%23f2f1c8&clientId=ub48347ea-cf88-4&from=paste&height=269&id=ubed67e22&originHeight=458&originWidth=964&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=49748&status=done&style=none&taskId=uf74a73cb-eb35-4a67-a8c1-6b3110b79d0&title=&width=566)

- 最终推送成功：

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382834559-82895845-9542-43cc-9c6c-c7d74d32e826.png#averageHue=%230d0906&clientId=ub48347ea-cf88-4&from=paste&height=106&id=uba1c95e3&originHeight=133&originWidth=995&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=23533&status=done&style=none&taskId=u8bb23fd2-5598-4f4a-869f-190a06167dc&title=&width=796)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/35382725/1693382877070-f36a5efd-efe0-40ac-8fc5-82c411ae4a8d.png#averageHue=%23fcfbfb&clientId=ub48347ea-cf88-4&from=paste&height=283&id=ubf172fd0&originHeight=354&originWidth=1334&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=45070&status=done&style=none&taskId=u7fb19d00-b97a-4e93-8bb4-7ed4dee2ea7&title=&width=1067.2)
