package de.erikschwob.fittrack.repository;

import de.erikschwob.fittrack.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    /**
     * Loads a workout together with its sets and their exercises in one query.
     * Used by the read endpoints so the response can be serialized after the
     * transaction closes ({@code open-in-view} is disabled) without lazy-loading.
     */
    @Query("""
            select distinct w from Workout w
            left join fetch w.sets s
            left join fetch s.exercise
            where w.id = :id
            """)
    Optional<Workout> findByIdWithSets(@Param("id") Long id);

    @Query("""
            select distinct w from Workout w
            left join fetch w.sets s
            left join fetch s.exercise
            order by w.performedOn desc
            """)
    List<Workout> findAllWithSets();

    @Query("""
            select distinct w from Workout w
            left join fetch w.sets s
            left join fetch s.exercise
            where w.performedOn between :from and :to
            order by w.performedOn desc
            """)
    List<Workout> findWithSetsBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
