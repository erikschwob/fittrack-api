package de.erikschwob.fittrack.service;

import de.erikschwob.fittrack.model.Exercise;
import de.erikschwob.fittrack.model.Workout;
import de.erikschwob.fittrack.model.WorkoutSet;
import de.erikschwob.fittrack.repository.ExerciseRepository;
import de.erikschwob.fittrack.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WorkoutService {

    private final WorkoutRepository workouts;
    private final ExerciseRepository exercises;

    public WorkoutService(WorkoutRepository workouts, ExerciseRepository exercises) {
        this.workouts = workouts;
        this.exercises = exercises;
    }

    public Workout create(LocalDate performedOn, String title, String notes) {
        return workouts.save(new Workout(performedOn, title, notes));
    }

    @Transactional(readOnly = true)
    public Workout get(Long id) {
        return workouts.findByIdWithSets(id)
                .orElseThrow(() -> new NotFoundException("Workout " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Workout> find(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return workouts.findWithSetsBetween(from, to);
        }
        return workouts.findAllWithSets();
    }

    /**
     * Appends a set to a workout. The set number is assigned automatically as the
     * next position within the session, so callers never have to track ordering.
     */
    public WorkoutSet addSet(Long workoutId, Long exerciseId, int reps, BigDecimal weightKg, Integer rpe) {
        Workout workout = get(workoutId);
        Exercise exercise = exercises.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercise " + exerciseId + " not found"));

        int nextSetNumber = workout.getSets().size() + 1;
        WorkoutSet set = new WorkoutSet(exercise, nextSetNumber, reps, weightKg, rpe);
        workout.addSet(set);
        workouts.save(workout);
        return set;
    }

    public void delete(Long id) {
        if (!workouts.existsById(id)) {
            throw new NotFoundException("Workout " + id + " not found");
        }
        workouts.deleteById(id);
    }
}
