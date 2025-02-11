# Cinetracker

## Overview

Cinetracker is a JavaFX-based application that allows users to catalog and analyze their watched cinematics, including movies, anime, and series. Users can import their personal cinematics with some data like the rating and the title and view additional details fetched from the OMDb API, such as cover images, director information, and IMDb ratings. The application also provides various statistical insights about the cinematics.

This project serves as a learning experience for developing GUI applications using JavaFX with the MVC architecture.

## Features

- Users can import cinematics data either via CSV files or manually through the application interface.
- The imported data includes:
  - Title of the cinematic
  - State (Finished, Watched, ToWatch, Watching)
  - Type (Movie, Anime, Series)
  - Rating (optional, as "ToWatch" does not have a rating yet)
- The application fetches additional details from the OMDb API, including actors, genres, and more.
- The dashboard offers multiple statistics, such as:
  - Average personal rating of all cinematics compared to the IMDb average rating.
  - Average personal rating of all actors in the cinematics compared to the IMDb average rating.
  - Average personal rating of all genres of the cinematics compared to the IMDb average rating.
  - Configurable filters to include/exclude specific states and types (e.g., statistics only for finished movies).
- A dedicated view for each type listing all corresponding cinematics.
- Users can export a CSV file with all watched cinematics.
- Logging functionality is available.


## Technologies Used

- **Programming Language:** Java, JavaFX
- **Testing Frameworks:** JUnit, Mockito, JQwik
- **Build & Development Tools:** Maven, IntelliJ IDEA
- **Database:** Local MySQL database running in a Docker container

## Prerequisites
To run this project, ensure you have the following installed:

### 1.Java
- Java 21 or later

### 2.MySQL Database (Docker)
- Install and run a MySQL container with the following command:
  ```sh
  docker run --name CineTrackDB -e MYSQL_ROOT_PASSWORD=yourpassword -p 3306:3306 -d mysql:latest

- Replace yourpassword with your desired root password.
- Ensure the database is accessible at localhost:3306.
- The SQL script required to set up the database schema can be found here [SQL](https://github.com/utuncel/CineTrack/blob/main/src/CineTrack/src/main/resources/Database-Script.txt).

### 3.OMDb API Key
- Obtain an API key from [OMDb API](https://www.omdbapi.com/apikey.aspx).

### 4.Environment Variables
Set the following environment variables:
  ```sh
CT_DB_PASSWORD=yourpassword
OMDb_API=your_api_key
  ```
- Replace `yourpassword` with your MySQL root password.
- Replace `your_api_key` with your OMDb API key.

## Architecture

- **Pattern:** MVCS (Model-View-Controller-Service)

## API Usage

- The application uses the **OMDb API** to fetch all data for the `ApiCinematic` class. More details can be found in the Javadoc: [ApiCinematic Documentation](https://utuncel.github.io/javadoc/CineTrack/org/com/model/domain/ApiCinematic.html)

## Testing

- The project includes **mocked unit tests** for the service layer.
- Repository methods are also tested.
- Some **property-based testing** approaches are included.

## Future Improvements

- Fix all warnings.
- Add integration tests.
- Add better error handling
- Implement additional statistics.

## Documentation

A Javadoc documentation is available at: [CineTrack Javadoc](https://utuncel.github.io/javadoc/CineTrack/module-summary.html)
