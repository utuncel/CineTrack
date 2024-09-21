package Models;

import Models.Helper.ApiCinematic;
import Models.Helper.Cinematic;
import Models.Helper.CsvCinematic;

public class Anime extends Cinematic {
    public Anime(ApiCinematic apiCinematic, CsvCinematic csvCinematic) {
        super(apiCinematic, csvCinematic);
    }
}
