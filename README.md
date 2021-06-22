# Divorce Fees and Payments Service [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This application is used for managing the fees for the Divorce application.

## Setup
Dummy
**Prerequisites**

- [JDK 11](https://openjdk.java.net/)
- [Docker](https://www.docker.com)

**Building**

The project uses [Gradle](https://gradle.org) as a build tool but you don't have to install it locally since there is a
`./gradlew` wrapper script.

To build project please execute the following command:

```bash
    ./gradlew build
```

**Running**

First you need to create distribution by executing following command:

```bash
    ./gradlew installDist
```

```bash
    ./gradlew bootRun
```

Now the API application will be exposing port `4009`

## Testing

**Unit tests**

To run all unit tests please execute following command:

```bash
    ./gradlew test
```

**Coding style tests**

To run all checks (including unit tests) please execute following command:

```bash
    ./gradlew check
```

**Mutation tests**

To run all mutation tests execute the following command:

```
./gradlew pitest

```

## Developing
**API documentation**

API documentation is provided with Swagger:
 - `http://localhost:4009/swagger-ui.html` - UI to interact with the API resources


**Versioning**

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

**Standard API**

We follow [RESTful API standards](https://hmcts.github.io/restful-api-standards/).

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
