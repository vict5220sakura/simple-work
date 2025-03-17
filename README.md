# simple-work

## 介绍
后台管理系统+多端开发app  
体验地址[http://39.104.14.161](http://39.104.14.161/simple-server/web/login), 也可以自行部署体验    
奉行简单化, 所有配置都可有可无, 比起动辄就要分布式的项目, 本项目要精简的多, 但是功能却应有尽有  
后台管理系统, 移动端  
富文本, 图片上传, 七牛缓存, 锁, 定时任务, 组织架构...
![后台管理](http://file.vict5220.top/LcHIMrpSc5__2025_03_13__15_56_30.png?attname=%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250313155609.png)
![APP(H5)](http://file.vict5220.top/Pa0aBE9ijw__2025_03_13__17_01_30.png?attname=%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250313165955.png)
## 运行环境
1. jdk1.8
2. node v16.17.0
3. uniapp(HBuilderX)

## 运行
#### simple-java-project(后端)
1. 创建mysql数据库 执行 dbScript文件夹下sql  
2. (可选)可以选择缓存/锁 模式, 如果使用redis作为缓存或锁, 需要创建redis数据库(默认无需创建)  
3. (可选)可以选择日志模式, 如果使用es作为日志存储库, 需要创建es(默认无需创建)  
4. 配置application.yml, 可以选择锁, 计数器, 缓存模式 (jdk or redis)(默认为jdk)  
5. 配置application-dev.yml, 配置mysql地址, 七牛配置, 以及是否开启静态文件缓存, redis地址(如果有), es地址(如果有)    
6. maven导入依赖
7. 启动项目 Application.java
8. 启动成功可以在控制台打开swagger页面

#### simple-web-project(web管理端)
1. 在simple-web-project目录中执行 npm install 
2. 运行 npm run vite
3. 打开浏览器 http://localhost:5175/simple-server/web/
4. 登录管理员账号 admin 123456

#### simple-app-project(app移动端)
1. 在simple-app-project目录中执行 npm install 
2. 配置 simple-app-project/src/manifest.json 文件 配置自己的appid
3. 配置 simple-app-project/src/MyEnv.ts 选择一个环境(默认为local)
4. 使用HBuilderX 打开项目
5. 使用HBuilderX 运行项目 (运行-> 运行到浏览器)
6. 打开页面 http://localhost:5174/simple-server/app/#/


## 说明
系统内ai功能需要使用阿里云百炼授权, 请自行申请, 自行配置(或者在在线体验地址体验)




