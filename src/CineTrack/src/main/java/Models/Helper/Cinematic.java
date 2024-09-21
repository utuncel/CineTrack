package Models.Helper;

import Models.Enums.State;
import Models.Enums.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Cinematic {
    private String title;
    private int myRating;
    private String description;
    private String imageUrl;
    private String runtime;
    private double otherRating;
    private String directorName;
    private List<String> genre;
    private State state;
    private Type type;

    public Cinematic(ApiCinematic apiCinematic, CsvCinematic csvCinematic) {
        this.title = csvCinematic.getTitle();
        this.myRating = csvCinematic.getRating();
        this.state = csvCinematic.getState();
        this.type = csvCinematic.getType();
        this.runtime = Optional.ofNullable(apiCinematic.getRuntime()).orElse("Unknown runtime");
        this.otherRating = Optional.ofNullable(apiCinematic.getImdbRating()).orElse(0.0);
        this.directorName = Optional.ofNullable(apiCinematic.getDirector()).orElse("Unknown director");
        this.description = Optional.ofNullable(apiCinematic.getPlot()).orElse("Unknown description");
        this.imageUrl = Optional.ofNullable(apiCinematic.getPosterUrl()).orElse("Unknown imageUrl");

        String genres = apiCinematic.getGenre();
        this.genre = (genres != null && !genres.isEmpty())
                ? Arrays.asList(genres.split(", "))
                : new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMyRating() {
        return myRating;
    }

    public void setMyRating(int myRating) {
        this.myRating = myRating;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

