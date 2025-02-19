# Springmail backend api

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

This project is part of a fullstack application of a simplified email platform. Below are some features presented in it

- Create users
- Security with jwt(JsonWebToken)
- Create chat conversations
- Reply messages in a chat
- Save chats in favorite's list
- Log implementation in api
- Cors configuration
- Automated tests
- Documentation with swagger

The authentication part is made using jwt and only one role "USERS", so if a user is logged in successfully he received the respective one

Here is the link of running application. If the api is offline send me an email because the host is free and it crashes sometimes
```bash
  https://springmail-backend.onrender.com
```

Swagger endpoint is in the following enpoint
```bash
  /swagger-ui.html
```

To run the application you can start by your IDE, but if you dont have java installed in your machine, use docker image to start

First you need to clone this repository and navigate into the folder generated
```bash
  git clone https://github.com/gabrielferreira02/springmail-backend.git
  cd springmail-backend
```
Then, generate the jar file of the application
```bash
  mvn clean install
```
Now you can build the docker image and use the project
```bash
  docker build -t springmail .
```
Start the project using
```bash
  docker run springmail
```

So, now that the project is running, to use the full application clone the repository of springmail frontend in the following link:
```bash
  https://github.com/gabrielferreira02/springmail-frontend
```
