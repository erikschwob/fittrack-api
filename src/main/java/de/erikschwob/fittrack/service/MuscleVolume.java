package de.erikschwob.fittrack.service;

import de.erikschwob.fittrack.model.MuscleGroup;

import java.math.BigDecimal;

/** Total training volume (Σ weight × reps) accumulated for one muscle group. */
public record MuscleVolume(MuscleGroup muscleGroup, BigDecimal volume) {
}
