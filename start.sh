#!/bin/sh

# 构建镜像
docker build -t myproject .

# 启动 MySQL 数据库容器，并将 SQL 文件映射到容器中
docker run -d --name mysql \
  -p 3307:3306 \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -e MYSQL_DATABASE=lalamove \
  mysql:latest

# 等待 MySQL 数据库容器启动
sleep 10


# 启动应用程序容器，并连接到 MySQL 数据库容器
docker run --name myproject -p 8080:8080 -d myproject

