
FROM gozap/oraclejdk8
LABEL maintainer=fengli

# /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime &&
RUN  echo 'Asia/Shanghai' >/etc/timezone

COPY ./ruoyi-admin/target/*.jar /app.jar



EXPOSE 8080

# hospital-manage
ENTRYPOINT ["/bin/sh","-c","java -Dfile.encoding=utf8  -Djava.security.egd=file:/dev/./urandom -jar /app.jar "]
