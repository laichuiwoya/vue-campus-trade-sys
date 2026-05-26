DROP DATABASE IF EXISTS campus_trade;
CREATE DATABASE campus_trade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE campus_trade;

CREATE TABLE sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  phone VARCHAR(20),
  avatar VARCHAR(255),
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  status TINYINT NOT NULL DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  sort INT NOT NULL DEFAULT 0,
  status TINYINT NOT NULL DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE goods (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  content TEXT,
  price DECIMAL(10,2) NOT NULL,
  original_price DECIMAL(10,2),
  cover VARCHAR(255),
  images TEXT,
  quantity INT NOT NULL DEFAULT 1,
  status VARCHAR(20) NOT NULL DEFAULT 'ON_SALE',
  view_count INT NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_goods_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
  CONSTRAINT fk_goods_category FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE favorite (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  goods_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_goods (user_id, goods_id),
  CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
  CONSTRAINT fk_favorite_goods FOREIGN KEY (goods_id) REFERENCES goods(id)
);

CREATE TABLE orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(32) NOT NULL UNIQUE,
  buyer_id BIGINT NOT NULL,
  seller_id BIGINT NOT NULL,
  goods_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
  remark VARCHAR(255),
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES sys_user(id),
  CONSTRAINT fk_orders_seller FOREIGN KEY (seller_id) REFERENCES sys_user(id),
  CONSTRAINT fk_orders_goods FOREIGN KEY (goods_id) REFERENCES goods(id)
);

CREATE TABLE order_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  note VARCHAR(255),
  operator_id BIGINT,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_order_log_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE comment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  goods_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content VARCHAR(500) NOT NULL,
  parent_id BIGINT,
  status TINYINT NOT NULL DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_comment_goods FOREIGN KEY (goods_id) REFERENCES goods(id),
  CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

INSERT INTO category(name, sort, status) VALUES
('Digital', 1, 1),
('Books', 2, 1),
('Daily Supplies', 3, 1),
('Sports', 4, 1);

-- password: 123456
INSERT INTO sys_user(username, password, nickname, role, status) VALUES
('admin', '$2a$10$/t9QaN8TpChSCbsGZeQq.ujR5ll5sZsFUcaox/FdaDdkRDVwfYm4O', 'Admin', 'ADMIN', 1),
('user01', '$2a$10$/t9QaN8TpChSCbsGZeQq.ujR5ll5sZsFUcaox/FdaDdkRDVwfYm4O', 'Student User', 'USER', 1);
