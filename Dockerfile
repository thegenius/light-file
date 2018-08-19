FROM airdock/oracle-jdk:1.8
COPY light-file-server-1.0.jar /app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar --endpoint http://127.0.0.1:9000  --access AKTAIOSFODNN7EXAMPLE  --secret wJalrXUtnFEMI/K7MDENG/bPxEfiCYEXAMPLEKEY --server.port=8080" ]
