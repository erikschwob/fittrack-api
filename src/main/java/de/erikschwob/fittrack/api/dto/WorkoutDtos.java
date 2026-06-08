package de.erikschwob.fittrack.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.erikschwob.fittrack.model.Workout;
import de.erikschwob.fittrack.model.WorkoutSet;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Request/response DTOs for workouts and their sets. */
public final class WorkoutDtos {

    private WorkoutDtos() {
    }

    public record CreateWorkoutRequest(
            @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate performedOn,
            @NotBlank String title,
            String notes) {
    }

    public record AddSetRequest(
            @NotNull Long exerciseId,
            @Positive @Max(100) int reps,
            @NotNull @DecimalMin("0.0") BigDecimal weightKg,
            @Min(1) @Max(10) Integer rpe) {
    }

    public record SetResponse(
            Long id,
            Long exerciseId,
            String exerciseName,
            int setNumber,
            int reps,
            BigDecimal weightKg,
            Integer rpe,
            BigDecimal estimatedOneRepMax) {

        public static SetResponse from(WorkoutSet s) {
            return new SetResponse(
                    s.getId(),
                    s.getExercise().getId(),
                    s.getExercise().getName(),
                    s.getSetNumber(),
                    s.getReps(),
                    s.getWeightKg(),
                    s.getRpe(),
                    s.estimatedOneRepMax());
        }
    }

    public record WorkoutResponse(
            Long id,
            LocalDate performedOn,
            String title,
            String notes,
            List<SetResponse> sets) {

        public static WorkoutResponse from(Workout w) {
            return new WorkoutResponse(
                    w.getId(),
                    w.getPerformedOn(),
                    w.getTitle(),
                    w.getNotes(),
                    w.getSets().stream().map(SetResponse::from).toList());
        }
    }
}
