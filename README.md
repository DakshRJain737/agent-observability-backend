# agent-observability-backend

# Python SDK
https://github.com/DakshRJain737/ai-agent-observatory-sdk

## application.properties
In the /agent-observatory/agent/src/main/ make a folder named resources and create an application.properties file and the following content to it.

```
spring.application.name=agent

spring.datasource.url=jdbc:postgresql://localhost:5432/agent
spring.datasource.username=POSTGRES_USERNAME
spring.datasource.password=POSTGRES_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.security.user.name=admin
spring.security.user.password=admin
```
Before running the application, create a database in postgres named ```agent``` or set:

```spring.jpa.hibernate.ddl-auto=create```
