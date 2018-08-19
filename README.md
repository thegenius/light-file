## This is a file server with docker

### Pre-require
```
jdk >= 1.8
docker >= 1.13.1
```

### Bootstrap
```
cd bin
chmod +x run.sh
./run.sh
```
That's it, have fun!

### Usage
```
    post http://localhost:8080/file
    get  http://localhost:8080/file?bucketName=xxx&fileName=xxx
```