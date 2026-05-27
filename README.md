# Campus Trade Sys

基于 Spring Boot + Vue 3 + MySQL 的校园二手交易平台，包含用户登录注册、商品发布、商品市场、收藏、订单、评论、图片上传和管理员后台等功能。

## 项目结构

```text
vue校园交易系统
├── campus-trade-server   # 后端 Spring Boot 项目
├── campus-trade-web      # 前端 Vue 3 + Vite 项目
└── README.md
```

## 技术栈

后端：

- Spring Boot 3.3.2
- Java 21
- MyBatis-Plus 3.5.7
- MySQL
- JWT
- BCrypt
- Maven

前端：

- Vue 3
- Vite
- Vue Router
- Pinia
- Axios
- Element Plus

## 主要功能

- 用户注册、登录、JWT 鉴权
- 商品列表、详情、搜索
- 商品发布、编辑、删除
- 商品库存 quantity 展示和下单扣减
- 商品封面图片上传
- 商品收藏
- 订单创建、订单列表、订单状态更新
- 商品评论
- 管理员后台
- 用户状态管理
- 商品状态管理
- 分类管理
- 跨域配置和静态上传文件访问

## 数据库说明

数据库初始化脚本：

```text
campus-trade-server/src/main/resources/sql/init.sql
```

脚本会创建 `campus_trade` 数据库，并初始化以下表：

- `sys_user`
- `category`
- `goods`
- `favorite`
- `orders`
- `order_log`
- `comment`

默认账号：

```text
管理员：admin / 123456
普通用户：user01 / 123456
```

数据库连接配置在：

```text
campus-trade-server/src/main/resources/application.yml
```

默认配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_trade?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

如本地 MySQL 账号或密码不同，请修改 `application.yml` 后再启动后端。

## 后端启动

进入后端目录：

```bash
cd campus-trade-server
```

安装依赖并启动：

```bash
mvn spring-boot:run
```

后端默认端口：

```text
http://localhost:8080
```

健康检查：

```text
GET http://localhost:8080/api/ping
```

## 前端启动

进入前端目录：

```bash
cd campus-trade-web
```

安装依赖：

```bash
npm install
```

启动开发服务器：

```bash
npm run dev
```

前端默认通过 Axios 请求后端接口：

```text
http://localhost:8080/api
```

配置位置：

```text
campus-trade-web/src/api/request.js
```

## 前后端连接方式

前端通过 Axios 调用后端 REST API。登录成功后，后端返回 JWT，前端保存到 `localStorage`，后续请求自动携带：

```text
Authorization: Bearer <token>
```

后端通过 `LoginInterceptor` 解析 token，并把当前用户信息写入请求属性，供 Controller 使用。

调用链路：

```text
Vue 页面
↓
src/api/modules.js
↓
src/api/request.js
↓
Spring Boot Controller
↓
Service
↓
Mapper
↓
MySQL
```

## 核心接口

认证：

- `POST /api/auth/register` 用户注册
- `POST /api/auth/login` 用户登录
- `GET /api/auth/me` 当前登录用户

商品：

- `GET /api/goods` 商品列表和搜索
- `GET /api/goods/{id}` 商品详情
- `POST /api/goods` 发布商品
- `PUT /api/goods` 修改商品
- `DELETE /api/goods/{id}` 删除商品

分类：

- `GET /api/categories` 分类列表

收藏：

- `GET /api/favorites` 收藏列表
- `POST /api/favorites` 添加收藏
- `DELETE /api/favorites/{id}` 取消收藏

订单：

- `GET /api/orders` 订单列表
- `POST /api/orders` 创建订单
- `PUT /api/orders` 更新订单

评论：

- `GET /api/comments` 评论列表
- `POST /api/comments` 发布评论

文件：

- `POST /api/files/upload` 上传图片

管理员：

- `GET /api/admin/users` 用户管理
- `PUT /api/admin/users/{id}/status` 修改用户状态
- `GET /api/admin/goods` 商品管理
- `PUT /api/admin/goods/{id}/status` 修改商品状态
- `GET /api/admin/orders` 订单管理
- `GET /api/admin/categories` 分类管理
- `POST /api/admin/categories` 新增分类
- `PUT /api/admin/categories` 修改分类
- `DELETE /api/admin/categories/{id}` 删除分类

## 前端页面

- `/` 商品市场
- `/login` 登录注册
- `/publish` 发布商品
- `/favorites` 我的收藏
- `/orders` 我的订单
- `/admin` 管理员后台

## 图片上传

图片上传接口会把文件保存到后端项目运行目录下的 `uploads` 文件夹，并通过下面的静态地址访问：

```text
http://localhost:8080/uploads/日期/文件名
```

相关代码：

- `campus-trade-server/src/main/java/com/campus/trade/controller/FileController.java`
- `campus-trade-server/src/main/java/com/campus/trade/config/WebConfig.java`

## 打包

后端打包：

```bash
cd campus-trade-server
mvn clean package
```

前端打包：

```bash
cd campus-trade-web
npm run build
```

## 注意事项

- 启动前请先执行 `init.sql` 初始化数据库。
- 后端默认端口是 `8080`。
- 前端 Axios 默认请求 `http://localhost:8080/api`。
- 管理员页面需要使用 `admin / 123456` 登录。
- 上传图片时需要确保后端服务正在运行。
