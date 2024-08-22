# onechat



## 개요

`onechat` is 100w instant messaging application

## 목적

JAVA 언어로 개발된 경량, 고성능, 단일 기계로 수십만에서 수백만의 온라인 사용자 IM을 지원합니다. 주요 목표는 인스턴트 메시징 문턱을 낮추고 빠르게 저비용 온라인 IM 시스템에 접속하는 것입니다. 매우 간결한 메시지 형식을 통해 여러 개의 서로 다른 프로토콜 간의 메시지 전송을 실현할 수 있습니다. 내장(예:
Http, Websocket, TCP 사용자 정의 IM 프로토콜)

### 구조

``` lua
onechat
├── im-common -- tools and common code
├── im-connector -- connection service
├── im-sdk -- send message function, encapsulate common module
├── im-server -- Business services
└── im-admin -- background management services
```

### Technology 

#### Backend technology

| Technology | Explanation  | website                                             |
|------------|-----------|------------------------------------------------|
| SpringBoot |  | https://spring.io/projects/spring-boot         |
| MyBatis    | ORM    | http://www.mybatis.org/mybatis-3/zh/index.html |
| Redis      | memory data storage    | https://redis.io/                              |
| Nginx      | static resource server   | https://www.nginx.com/                         |
| MinIO      | object storage      | https://github.com/minio/minio                 |

#### diagram


### Development environment

| Tools     | version | website                                                                                                |
|-----------|-------|---------------------------------------------------------------------------------------------------|
| JDK       | 11    | https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html                      |
| Mysql     | 8     | https://www.mysql.com/                                                                            |
| Redis     | 7.0   | https://redis.io/download                                                                         |
| Nginx     | 1.22  | http://nginx.org/en/download.html                                                                 |
| Zookeeper | 3.8.0 | https://www.apache.org/dyn/closer.lua/zookeeper/zookeeper-3.8.0/apache-zookeeper-3.8.0-bin.tar.gz |

### Development environment

> Windows

- Clone the onechat project and import it into IDEA to complete the compilation
- Start the long connection service [im-connector]
- Start the business service [im-server]

### Screenshot



## Contributing




