#!/bin/bash


mkdir -p /root/minio/data
mkdir -p /root/minio/config

docker stop miniofile
docker rm miniofile
docker stop lightfile
docker rm lightfile

docker pull minio/minio
docker build -t lvonce/light-file-server:1.0 . 

docker run -d -p 8080:8080 -p 9000:9000 --name miniofile \
  -e "MINIO_ACCESS_KEY=AKTAIOSFODNN7EXAMPLE" \
  -e "MINIO_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxEfiCYEXAMPLEKEY" \
  -v /root/minio/data:/data \
  -v /root/minio/config:/root/.minio \
  minio/minio server /data

docker run -d --net=container:miniofile --name lightfile -t lvonce/light-file-server:1.0 

docker ps
