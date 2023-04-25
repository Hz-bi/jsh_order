


# 使用官方的maven镜像作为构建工具
FROM maven:3.8-jdk-11-slim AS build

# 将源码复制到容器内
COPY . /app
WORKDIR /app

# 打包应用
RUN mvn clean package -DskipTests

# 使用openjdk镜像作为应用运行环境
FROM openjdk:11-jre-slim

# 将jar文件复制到容器内
COPY --from=build /app/target/*.jar /app.jar


# 暴露端口
EXPOSE 8080

# 容器启动命令
CMD ["java", "-jar", "/app.jar"]
