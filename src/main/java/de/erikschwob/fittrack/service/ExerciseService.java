package de.erikschwob.fittrack.service;

import de.erikschwob.fittrack.model.Exercise;
import de.erikschwob.fittrack.model.MuscleGroup;
import de.erikschwob.fittrack.repository.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExerciseService {

    private final ExerciseRepository exercises;

    public ExerciseService(ExerciseRepository exercises) {
        this.exercises = exercises;
    }

    @Transactional(readOnly = true)
    public List<Exercise> findAll(MuscleGroup muscleGroup) {
        return muscleGroup == null ? exercises.findAll() : exercises.findByMuscleGroup(muscleGroup);
    }

    @Transactional(readOnly = true)
    public Exercise get(Long id) {
        return exercises.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercise " + id + " not found"));
    }

    public Exercise create(Exercise exercise) {
        if (exercises.existsByNameIgnoreCase(exercise.getName())) {
            throw new DuplicateException("Exercise '" + exercise.getName() + "' already exists");
        }
        return exercises.save(exercise);
    }
}
