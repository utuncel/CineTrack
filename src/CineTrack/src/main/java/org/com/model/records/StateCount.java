package org.com.model.records;

/**
 * A record representing the count of cinematics in different states.
 *
 * <p>This record is used to store the number of cinematics categorized by their
 * current state, such as finished, dropped, currently watching, or planned to watch.</p>
 *
 * @param finished The count of finished cinematics.
 * @param dropped  The count of cinematics that have been dropped.
 * @param watching The count of cinematics currently being watched.
 * @param towatch  The count of cinematics planned to be watched.
 * @author umut
 * @version 1.0
 */
public record StateCount(int finished, int dropped, int watching, int towatch) {

}
