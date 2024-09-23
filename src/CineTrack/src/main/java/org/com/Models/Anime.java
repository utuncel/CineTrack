package org.com.Models;

import org.com.Models.Helper.ApiCinematic;
import org.com.Models.Helper.Cinematic;
import org.com.Models.Helper.CsvCinematic;

public class Anime extends Cinematic {
    public Anime(ApiCinematic apiCinematic, CsvCinematic csvCinematic) {
        super(apiCinematic, csvCinematic);
    }
}
