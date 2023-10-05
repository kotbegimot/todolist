# Todolist backend
### Overview
This application is a stateful service for To-Dos managing, with ability to store data in MySQL DB which allows to
create, get, edit and delete to-do objects.

This backend service manages and stores information about to-do and tasks they contain via API endpoints.
It accepts information via body of JSON and returns response in the same format. For detailed specification see below.
This service implemented following the MVC pattern and has a controller, service and data persistence layers.

Open API documentation can be viewed via [Swagger UI](http://localhost:8080/swagger-ui/index.html)
after application start.

### System requirements
The application is containerized with all dependencies being obtained on start.

In order to run application Docker is required.

_Note:_ For Mac OS, once the repository content is cloned, make sure that the folder with the application
is exposed to Docker via File Share to ensure the image can be mounted.

### How to run
To run the application execute from the application folder: ```docker-compose up```

The application will run on http://localhost:8080.

The DB can be accessed via [mysql://localhost:3306/todo_db](mysql://localhost:3306/todo_db).
It is pre-filled with mock data for test purposes via `init.sql` in the app root folder.

### How to check app build

```mvn clean install```

### Development requirements
- Java 17
- Spring Boot 3.0.0^
- Maven
- MySQL DB 8.0.0^

### How to check tests coverage with Sonar
1. Get Jacoco report:
```mvn test```
2. Start Sonar server locally: 
```.\StartSonar.bat```
3. Launch Sonar on Maven: 
```mvn sonar:sonar```
4. Go to the dashboard link from the build:
   [Sonar Dashboard](http://localhost:9000/dashboard?id=todolist)


## Notes on implementation
### Business logic
Some notes for application improvement
- **Expand to-dos filed**: add dates, level of urgency;
- **To-dos duplication**: there is no check if a to-do already exists on create; 
- **Tasks management**: an ability to manage tasks separately from to-dos;
- **Validation**: More deep validation;
- **Filters**: Add filters: by dates, by to-do/task names
- 
### Technical implementation
Due to time constraints I was not able to implement the following:
- More Unit tests
- Integration tests for repository
- Todos create, update and delete on front-end side