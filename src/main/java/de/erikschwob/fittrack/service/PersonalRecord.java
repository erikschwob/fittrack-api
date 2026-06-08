package de.erikschwob.fittrack.service;

import java.math.BigDecimal;

/**
 * The best estimated one-rep max achieved for one exercise, and the set it came
 * from. "Estimated" because it is derived via the Epley formula rather than an
 * actual single-rep attempt.
 */
public record PersonalRecord(
        Long exerciseId,
        String exerciseName,
        BigDecimal estimatedOneRepMax,
        int reps,
        BigDecimal weightKg) {
}
