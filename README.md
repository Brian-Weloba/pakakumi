# Pakakumi Tracker

[![GitHub license](https://img.shields.io/github/license/brian-weloba/pakakumi)](https://github.com/brian-weloba/pakakumi/blob/main/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/brian-weloba/pakakumi)](https://github.com/brian-weloba/pakakumi/issues)
[![GitHub stars](https://img.shields.io/github/stars/brian-weloba/pakakumi)](https://github.com/brian-weloba/pakakumi/stargazers)

Pakakumi Tracker is a web application that tracks trends for all Pakakumi online betting games over time. The app uses Postgres as its database and scrapes the Pakakumi site for data.

## Prerequisites

Before running the app, you'll need to have the following installed on your machine:

- Java 17 or later
- Maven
- Postgres or another relational database

## Getting Started

1. Clone this repository to your local machine.

2. Create a Postgres database for the app.

3. In the `application.properties` file, set the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties to match your database configuration.

4. Open a terminal and navigate to the project directory.

5. Run `mvn spring-boot:run` to start the app.

6. Open a web browser and go to `http://localhost:8080` to view the app.

## Usage

To use the app, simply open it in your web browser. The app will automatically scrape the Pakakumi site for data and display trends for all games over time using a line chart. The chart will automatically update every few seconds to show the latest trends.

## Contributing

Contributions are welcome! If you find a bug or want to suggest a new feature, please create an issue on GitHub. If you want to contribute code, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
