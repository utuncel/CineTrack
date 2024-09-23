package org.com.Models;

import org.com.Models.Helper.ApiCinematic;
import org.com.Models.Helper.Cinematic;
import org.com.Models.Helper.CsvCinematic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieModel extends Cinematic {
    private List<String> actors;

    public MovieModel(ApiCinematic apiCinematic, CsvCinematic csvCinematic) {
        super(apiCinematic, csvCinematic);


        String actors = apiCinematic.getActors();
        this.actors = (actors != null && !actors.isEmpty())
                ? Arrays.asList(actors.split(", "))
                : new ArrayList<>();
    }
}
