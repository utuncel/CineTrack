package Service;

import Models.*;
import Models.Helper.ApiCinematic;
import Models.Helper.CinematicCreator;
import Models.Helper.CsvCinematic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CineFactory {
    private final CsvImporter csvImporter;
    private final ApiData apiData;
    CinematicCreator cinematicCreator;

    public CineFactory(CsvImporter csvImporter, ApiData apiData) {
        this.csvImporter = csvImporter;
        this.apiData = apiData;
        cinematicCreator = new CinematicCreator();
    }

    public List<Cinematic> createCinematics() throws IOException {
        List<CsvCinematic> csvCinematics = csvImporter.importData();
        final List<Cinematic> createdCinematics = new ArrayList<>();
        final List<String> notFoundTitles = new ArrayList<>();

        for (CsvCinematic csvCinematic : csvCinematics) {
           cinematicMerge(csvCinematic, createdCinematics, notFoundTitles);
        }

        handleNotFoundTitles(notFoundTitles);

        return createdCinematics;
    }

    private void cinematicMerge(CsvCinematic csvCinematic, List<Cinematic> createdCinematics, List<String> notFoundTitles){
        ApiCinematic apiCinematic = apiData.fetchMoviesOrSeries(csvCinematic.getTitle());

        if (apiCinematic == null) {
            notFoundTitles.add(csvCinematic.getTitle());
        } else {
            Cinematic cinematic = cinematicCreator.createCinematic(apiCinematic, csvCinematic);
            createdCinematics.add(cinematic);
        }
    }

    private void handleNotFoundTitles(List<String> notFoundTitles) {
        if (!notFoundTitles.isEmpty()) {
            System.err.println("Following Titles were not found");
            for (String title : notFoundTitles) {
                System.err.println("- " + title);
            }
        }
    }
}

