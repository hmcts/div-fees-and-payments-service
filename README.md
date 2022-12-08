# Divorce Fees and Payments Service [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This application is used for managing the fees for the Divorce application.

## Setup

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
```bash
./gradlew pitest
```

### Running additional tests in the Jenkins PR Pipeline

1. Add one or more appropriate labels to your PR in GitHub. Valid labels are:

- ```enable_fortify_scan```
- ```enable_security_scan```
- ```enable_full_functional_tests```

2. Trigger a build of your PR in Jenkins.  Fortify scans will take place asynchronously as part of the Static Checks/Container Build step.
- Check the Blue Ocean view for live monitoring, and review the logs once complete for any issues.
- As Fortify scans execute during the Static Checks/Container Build step, you will need to ensure this is triggered by making a minor change to the PR, such as bumping the chart version.

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

