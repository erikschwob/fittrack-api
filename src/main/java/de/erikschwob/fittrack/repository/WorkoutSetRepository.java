package de.erikschwob.fittrack.repository;

import de.erikschwob.fittrack.model.ExerciseCategory;
import de.erikschwob.fittrack.model.MuscleGroup;
import de.erikschwob.fittrack.model.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {

    /**
     * All sets of a given exercise category, eagerly joined with their exercise
     * so personal records can be computed without lazy-loading surprises.
     */
    @Query("select s from WorkoutSet s join fetch s.exercise e where e.category = :category")
    List<WorkoutSet> findByCategoryWithExercise(@Param("category") ExerciseCategory category);

    /**
     * Aggregated training volume (Σ weight × reps) per muscle group within a date
     * range. Pushed down to SQL — both PostgreSQL and H2 evaluate this natively.
     */
    @Query("""
            select s.exercise.muscleGroup as muscleGroup, sum(s.weightKg * s.reps) as volume
            from WorkoutSet s
            where s.workout.performedOn between :from and :to
            group by s.exercise.muscleGroup
            order by sum(s.weightKg * s.reps) desc
            """)
    List<MuscleVolumeRow> volumeByMuscleGroup(@Param("from") LocalDate from, @Param("to") LocalDate to);

    /** Projection for the volume-per-muscle-group aggregate query. */
    interface MuscleVolumeRow {
        MuscleGroup getMuscleGroup();

        BigDecimal getVolume();
    }
}
