## 未使用 git add 缓存代码时
git checkout -- <filename>
git rm --cached “文件路径”不删除物理文件，仅将该文件从缓存中删除；
git rm --f “文件路径”，不仅将该文件从缓存中删除，还会将物理文件删除
## 撤回暂存区所有文件
git reset HEAD .
撤回暂存区指定的文件
git reset HEAD filename
## 撤销commit
git reset --soft HEAD^ 
git reset --hard HEAD^ 
git reset --hard commit_id

## 分支
https://blog.csdn.net/csflvcxx/article/details/81612336
创建新分支：git branch branchName
切换到新分支：git checkout branchName
上面两个命令也可以合成为一个命令：
git checkout -b branchName


+ data matrix
    + cancer (癌症)[ESCA、BRAC...]
    + study(研究)[mRNA、miRNA、lncRNA...]
    + database(数据来源)[TCGA、GEO...]
    + dataCategory(数据分类)[FPKM、Counts、clinical...]
    + analysisSoftware(分析软件)[limma、DESeq2...]
    + dataId[数据Id](GSEXXX...)

```
spring:
#  data:
#    mongodb:
#      uri: mongodb://localhost:27017/bioinfo
  http:
    encoding:
      charset=UTF-8:
  jpa:
    hibernate.ddl-auto: update
    show-sql: true
    open-in-view: false
    properties:
      hibernate:
        enable_lazy_load_no_trans: true

  servlet:
    multipart:
      max-file-size: 1000MB
      max-request-size: 1000MB
  mail:                             #邮箱配置
    host: smtp.163.com              #使用163邮箱服务器
    username: XXXX@163.com   #你的163邮箱
    password: XXXXXXXX      #SMTP授权密码 (不是密码)
    port: 25
    protocol: smtp
    default-encoding: UTF-8
    jndi-name: email
############################## mysql start##################################
#  datasource:
#    driver-class-name: com.mysql.cj.jdbc.Driver
#    url: jdbc:mysql://localhost:3306/bioinfo?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
#    username: root
#    password: 123456
############################## mysql end##################################

############################## h2 start ##################################
# 使用H2数据库
  datasource:
    platform: h2
    driverClassName: org.h2.Driver
    url: jdbc:h2:file:~/Downloads/h2/dbc2m
    username: root
    password: 123456
#    schema: classpath:db/user.sql #启动时需要初始化的建表语句
#    data: classpath:init_data.sql #初始化的数据
    initialization-mode: always
  h2:
    console:
      settings:
        web-allow-others: true # 配置h2的远程访问
      enabled: true # 配置程序开启时就会启动h2 web consloe
      path: /h2-console #，进行该配置，你就可以通过YOUR_URL/h2-console访问h2 web consloe。YOUR_URL是你程序的访问URl
############################## h2 end ##################################

jwt:
  base64-secret: ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=
bioinfo:
    workDir: ${user.home}/.bioinfo

#https://www.jianshu.com/p/35878d4ec130
server:
  tomcat:
    max-threads: 200
    max-connections: 300
  port: 8080
https:
  ssl:
    key-store: classpath:sample.pfx #配置证书路径
    key-store-password: 3bWBoh69 #证书密码
  port: 8081
```

```
  @Autowired
    ConcurrentMapCacheManager concurrentMapCacheManager;
    Cache cache = concurrentMapCacheManager.getCache("TERM");
```