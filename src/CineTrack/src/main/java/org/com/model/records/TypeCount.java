package org.com.model.records;

/**
 * A record representing the count of different types of cinematics.
 *
 * <p>This record is used to store the number of movies, series, and anime
 * for analytics or display purposes in the application.</p>
 *
 * @param movie  The count of movies.
 * @param series The count of series.
 * @param anime  The count of anime.
 */
public record TypeCount(int movie, int series, int anime) {

}
