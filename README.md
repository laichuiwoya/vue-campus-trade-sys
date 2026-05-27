# Campus Trade Sys

A campus second-hand trading platform built with Spring Boot, Vue 3, and MySQL.
The project contains user authentication, product publishing, product browsing,
favorites, orders, comments, image upload, inventory handling, and an admin panel.

## Project Structure

```text
vue-campus-trade-system
+-- campus-trade-server   # Spring Boot backend
+-- campus-trade-web      # Vue 3 + Vite frontend
+-- README.md
```

## Tech Stack

Backend:

- Spring Boot 3.3.2
- Java 21
- MyBatis-Plus 3.5.7
- MySQL
- JWT
- BCrypt
- Maven

Frontend:

- Vue 3
- Vite
- Vue Router
- Pinia
- Axios
- Element Plus

## Features

- User registration and login
- JWT authentication
- Product list, detail, and keyword search
- Product publishing, editing, and deletion
- Product cover image upload
- Product inventory display and order deduction through `quantity`
- Favorite products
- Order creation, order list, and order status update
- Product comments
- Admin user management
- Admin product status management
- Admin category management
- CORS configuration
- Static access for uploaded files

## Database

The initialization SQL file is:

```text
campus-trade-server/src/main/resources/sql/init.sql
```

It creates the `campus_trade` database and these tables:

- `sys_user`
- `category`
- `goods`
- `favorite`
- `orders`
- `order_log`
- `comment`

Default accounts:

```text
Admin: admin / 123456
User:  user01 / 123456
```

Database connection config:

```text
campus-trade-server/src/main/resources/application.yml
```

Default MySQL config:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_trade?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

If your local MySQL username or password is different, update
`application.yml` before starting the backend.

## Backend Setup

Run the database initialization script first:

```text
campus-trade-server/src/main/resources/sql/init.sql
```

Start the backend:

```bash
cd campus-trade-server
mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

Health check:

```text
GET http://localhost:8080/api/ping
```

## Frontend Setup

Install dependencies and start the Vite dev server:

```bash
cd campus-trade-web
npm install
npm run dev
```

The frontend calls the backend through Axios with this base URL:

```text
http://localhost:8080/api
```

Config file:

```text
campus-trade-web/src/api/request.js
```

## Frontend And Backend Connection

The Vue frontend calls Spring Boot REST APIs through Axios.
After login, the backend returns a JWT token. The frontend stores it in
`localStorage` and attaches it to later requests:

```text
Authorization: Bearer <token>
```

The backend `LoginInterceptor` parses the token and writes the current user
information into request attributes for controllers.

Request flow:

```text
Vue page
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

## Main APIs

Authentication:

- `POST /api/auth/register` Register
- `POST /api/auth/login` Login
- `GET /api/auth/me` Current user

Products:

- `GET /api/goods` Product list and search
- `GET /api/goods/{id}` Product detail
- `POST /api/goods` Create product
- `PUT /api/goods` Update product
- `DELETE /api/goods/{id}` Delete product

Categories:

- `GET /api/categories` Category list

Favorites:

- `GET /api/favorites` Favorite list
- `POST /api/favorites` Add favorite
- `DELETE /api/favorites/{id}` Remove favorite

Orders:

- `GET /api/orders` Order list
- `POST /api/orders` Create order
- `PUT /api/orders` Update order

Comments:

- `GET /api/comments` Comment list
- `POST /api/comments` Create comment

Files:

- `POST /api/files/upload` Upload image

Admin:

- `GET /api/admin/users` User management
- `PUT /api/admin/users/{id}/status` Update user status
- `GET /api/admin/goods` Product management
- `PUT /api/admin/goods/{id}/status` Update product status
- `GET /api/admin/orders` Order management
- `GET /api/admin/categories` Category management
- `POST /api/admin/categories` Create category
- `PUT /api/admin/categories` Update category
- `DELETE /api/admin/categories/{id}` Delete category

## Frontend Routes

- `/` Product market
- `/login` Login and register
- `/publish` Publish product
- `/favorites` My favorites
- `/orders` My orders
- `/admin` Admin panel

## Image Upload

Uploaded images are stored under the backend runtime directory:

```text
uploads/yyyy-MM-dd/file-name
```

They are exposed through:

```text
http://localhost:8080/uploads/yyyy-MM-dd/file-name
```

Related files:

- `campus-trade-server/src/main/java/com/campus/trade/controller/FileController.java`
- `campus-trade-server/src/main/java/com/campus/trade/config/WebConfig.java`

## Build

Backend:

```bash
cd campus-trade-server
mvn clean package
```

Frontend:

```bash
cd campus-trade-web
npm run build
```

## Notes

- Run `init.sql` before starting the backend.
- Backend port is `8080`.
- Frontend API base URL is `http://localhost:8080/api`.
- Use `admin / 123456` to access the admin panel.
- Keep the backend running when uploading images from the frontend.
