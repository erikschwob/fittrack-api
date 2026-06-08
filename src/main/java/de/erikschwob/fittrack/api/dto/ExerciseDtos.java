package de.erikschwob.fittrack.api.dto;

import de.erikschwob.fittrack.model.Exercise;
import de.erikschwob.fittrack.model.ExerciseCategory;
import de.erikschwob.fittrack.model.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Request/response DTOs for the exercise catalogue. */
public final class ExerciseDtos {

    private ExerciseDtos() {
    }

    public record CreateExerciseRequest(
            @NotBlank @Size(max = 120) String name,
            @NotNull MuscleGroup muscleGroup,
            @NotNull ExerciseCategory category,
            @Size(max = 500) String description) {

        public Exercise toEntity() {
            return new Exercise(name, muscleGroup, category, description);
        }
    }

    public record ExerciseResponse(
            Long id,
            String name,
            MuscleGroup muscleGroup,
            ExerciseCategory category,
            String description) {

        public static ExerciseResponse from(Exercise e) {
            return new ExerciseResponse(e.getId(), e.getName(), e.getMuscleGroup(), e.getCategory(), e.getDescription());
        }
    }
}
