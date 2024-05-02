# 基础镜像
FROM node:14.15.1-alpine as build-stage

# 设置工作目录
WORKDIR /app

# 拷贝 package.json 和 package-lock.json 到工作目录
COPY ./ruoyi-ui/package*.json ./


# 设置npm的镜像源为淘宝镜像
RUN npm config set registry https://registry.npm.taobao.org/


# 安装依赖
RUN npm install

# 拷贝所有文件到工作目录
COPY ./ruoyi-ui .

# 构建生产环境的代码
RUN npm run build:prod

# 生产环境镜像
FROM nginx:1.19.6-alpine

# 将构建好的代码拷贝到 Nginx 的默认目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 复制自定义 Nginx 配置文件到容器中
COPY ./ruoyi-ui/nginx.conf /etc/nginx/conf.d/default.conf

# 暴露80端口
EXPOSE 80

# 启动Nginx服务
CMD ["nginx", "-g", "daemon off;"]
