package de.erikschwob.fittrack.repository;

import de.erikschwob.fittrack.model.Exercise;
import de.erikschwob.fittrack.model.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);

    Optional<Exercise> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
