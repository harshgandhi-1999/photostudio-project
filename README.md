# Spring boot rest api project

Portal for the photographers to upload their content - create photos,albums,edit profile.

### Add below code in your application.yaml file

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/${your_db_name}
    username: ${mysql_username}
    password: ${mysql_password}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
jwt:
  JWT_SECRET: abcd
  EXPIRATION_TIME: 43200000 #1hr
logging:
  level:
    org:
      springframework:
        security: DEBUG
cloudinary:
  cloud_name: ${cloudinary_cloud_name}
  api_key: ${cloudinary_api_key}
  api_secret: ${cloudinary_api_secret}
```
