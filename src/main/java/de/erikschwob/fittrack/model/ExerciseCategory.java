package de.erikschwob.fittrack.model;

/**
 * Broad movement category, used to decide which metrics are meaningful
 * (e.g. estimated one-rep-max only makes sense for STRENGTH movements).
 */
public enum ExerciseCategory {
    STRENGTH,
    CARDIO,
    MOBILITY
}
