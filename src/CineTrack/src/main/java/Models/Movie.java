package Models;

import Models.Helper.ApiCinematic;
import Models.Helper.Cinematic;
import Models.Helper.CsvCinematic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie extends Cinematic {
    private List<String> actors;

    public Movie(ApiCinematic apiCinematic, CsvCinematic csvCinematic) {
        super(apiCinematic, csvCinematic);


        String actors = apiCinematic.getActors();
        this.actors = (actors != null && !actors.isEmpty())
                ? Arrays.asList(actors.split(", "))
                : new ArrayList<>();
    }
}
