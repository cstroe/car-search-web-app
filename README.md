# VanSearch

A simple web app to keep track of vehicles for sale.
You're probably better off using a spreadsheet.

<table>
<tr>
    <td><img alt="A screenshot of the main screen of the web app showing a list of vehicles"
             src="docs/screenshot-20241205.png"/></td>
</tr>
</table>

## Requirements

* JDK 8+: can be obtained from [AdoptOpenJDK](https://adoptopenjdk.net/) and other sources.

## Usage

### Database

Start a new Postgres database using `docker-compose`: 

```shell
docker-compose up -d
```

After the database is up, you can restore a database snapshot (if you have one):

```shell
./restore_database.sh <path to .sql.gz backup>
```

### Webapp

Run the web application using:

```shell
./gradlew run
```

Look on the console to see the URL where the web app is listening.

### Backup the database

Run:

```shell
./backup_database.sh
```

## Testing

Unit tests are automatically run as part of the `build` task.  You can access test reports in the `build/reports/test` directory:

```
xdg-open build/reports/test/index.html
```

NOTE: On MacOS, use `open` instead of `xdg-open`.
