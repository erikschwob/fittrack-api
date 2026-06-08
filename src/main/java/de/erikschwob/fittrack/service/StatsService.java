package de.erikschwob.fittrack.service;

import de.erikschwob.fittrack.model.ExerciseCategory;
import de.erikschwob.fittrack.model.WorkoutSet;
import de.erikschwob.fittrack.repository.WorkoutSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Read-only analytics derived from logged sets: personal records and training
 * volume. The estimated-1RM ranking is computed in Java because the Epley
 * formula is awkward to express portably in JPQL; the volume aggregate is pushed
 * down to SQL.
 */
@Service
@Transactional(readOnly = true)
public class StatsService {

    private final WorkoutSetRepository sets;

    public StatsService(WorkoutSetRepository sets) {
        this.sets = sets;
    }

    /**
     * For every strength exercise that has at least one logged set, the set with
     * the highest estimated one-rep max. Sorted by exercise name for stable output.
     */
    public List<PersonalRecord> personalRecords() {
        List<WorkoutSet> strengthSets = sets.findByCategoryWithExercise(ExerciseCategory.STRENGTH);

        Map<Long, WorkoutSet> bestByExercise = strengthSets.stream()
                .collect(Collectors.toMap(
                        s -> s.getExercise().getId(),
                        s -> s,
                        (a, b) -> a.estimatedOneRepMax().compareTo(b.estimatedOneRepMax()) >= 0 ? a : b));

        return bestByExercise.values().stream()
                .map(s -> new PersonalRecord(
                        s.getExercise().getId(),
                        s.getExercise().getName(),
                        s.estimatedOneRepMax(),
                        s.getReps(),
                        s.getWeightKg()))
                .sorted(Comparator.comparing(PersonalRecord::exerciseName))
                .toList();
    }

    /** Training volume per muscle group within an inclusive date range. */
    public List<MuscleVolume> volumeByMuscleGroup(LocalDate from, LocalDate to) {
        return sets.volumeByMuscleGroup(from, to).stream()
                .map(row -> new MuscleVolume(row.getMuscleGroup(), row.getVolume()))
                .toList();
    }
}
