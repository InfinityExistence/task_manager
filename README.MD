# Система управления задачами
Этот проект представляет собой простую систему управления задачами (Task Management System), разработанную на Java. Система позволяет взаимодействовать с задачами, оставлять к ним комментарии. Вывод данных сделан постранично. 

Аутентификация и авторизация осуществляется с помощью Jwt токена ("Bearer " токен передается в заголовке Authorization.
Так же возможна регистрация и аутентификация с помощью почты и пароля.

## Стек технологий
- Java 21
- Spring boot
- Spring security + Nimbus jose jwt
- Spring web
- Hibernate + JPA
- Swagger-ui
- Lombok
- PostgreSQL
- Docker-compose

## Сборка

`docker-compose build`
 
или

`docker compose build`

## Запуск
`docker-compose up -d`

или

`docker compose up -d`

## Остановка

`docker-compose down`

или

`docker compose down`
## Swagger-ui

[Эндпоинт swagger-ui с подробной документацией </br>(Необходимо запустить контейнер с приложением)](http://localhost:3005/api/swagger/swagger-ui/index.html)

## Стандартные пользователи для проверки системы:

---
`{
    "email" : "manager@gmail.com",
    "password" : "managermanager"
}`
---
`{
    "email" : "programmer@gmail.com",
    "password" : "programmerprogrammer"
}`
---
`{
    "email" : "tester@gmail.com",
    "password" : "testertester"
}`
---