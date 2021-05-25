## Location Conquer Game

Simple REST API for mini game to conquer map location points with a mobile phone. London is the game’s location.

There are location points, that are able to be conquered. A user can conquer a point only if it’s not conquered and if a user is located near it (not more than 15 meters away). After user conquers a point his score is increased by one point.

Possible actions:
* Get a location point in a certain area
* Conquer a location point
* Show your score

Usage of the DB is NOT needed (a requirement), implementation is done using collections.

### Implementation

The system is build using Spring Boot and Java 1.8. The following modules are included to build the app:
* Web
* Lombok

The system does not have db layer, and all interactions are done on the service layer.

The controllers expose API endpoints to accomplish operations stated at the beginning. There are two endpoint roots:

- `/locations` - operations related to locations
    * `GET /locations/all` - returns list of all locations near users current location (paginated)
    * `GET /locations/all-open` - returns list of all locations yet to be conquered near users current location (paginated)
    * `PUT /locations/{locationId}` - conquers a location and increases user points
- `/users` - operations related to users
    * `GET /users/{userId}/points` - returns user points

### Building, Running and Testing

```bash
$ ./mvnw clean spring-boot:run
```
or alternatively using your installed maven version

```bash
mvn clean spring-boot:run
```

To see the application in action you can use `swagger` collection provided.

### Dockerizing app

```bash
$ mvn clean package
$ docker build -t ognio-conquer-game:latest .
```

running docker image

```bash
$ docker run -it -p 8080:8080 ognio-conquer-game
```

### Future improvements

* use JWT for securing application
* use a database schema provided below


| User |
|------|
| id PK |
| username IX |
| score |

| Location |
|-----|
| id PK |
| latitude |
| longitude |
| marked |

| UserLocation |
|-----|
| id PK |
| userId FK |
| locationId FK |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

