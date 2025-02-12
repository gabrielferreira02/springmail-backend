# Springmail backend api

This project is part of a fullstack application of a simplified email platform. Below are some features presented in it

- Create users
- Security with jwt(JsonWebToken)
- Create chat conversations
- Reply messages in a chat
- Save chats in favorite's list
- Log implementation in api
- Cors configuration
- Automated tests

The authentication part is made using jwt and only one role "USERS", so if a user is logged in successfully he received the respective one

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
