# VanSearch

A Scala project built using Gradle

## Requirements

* JDK 8+: can be obtained from [AdoptOpenJDK](https://adoptopenjdk.net/) and other sources.

## Usage

### Database

Start a new Postgres database using `docker-compose`: 

```shell
docker-compose up -d
```

### Webapp

Run the web application using:

```shell
./gradlew run
```

Look on the console to see the URL where the web app is listening.

## Testing

Unit tests are automatically run as part of the `build` task.  You can access test reports in the `build/reports/test` directory:

```
xdg-open build/reports/test/index.html
```

NOTE: On MacOS, use `open` instead of `xdg-open`.
