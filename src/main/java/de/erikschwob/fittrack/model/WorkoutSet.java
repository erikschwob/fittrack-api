package de.erikschwob.fittrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * One working set: a number of reps at a given load, optionally rated by RPE
 * (rate of perceived exertion, 1–10). The atom from which all stats are derived.
 */
@Entity
@Table(name = "workout_set")
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "set_number", nullable = false)
    private int setNumber;

    @Column(nullable = false)
    private int reps;

    @Column(name = "weight_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal weightKg;

    /** Rate of perceived exertion, 1–10. Optional. */
    @Column
    private Integer rpe;

    protected WorkoutSet() {
        // for JPA
    }

    public WorkoutSet(Exercise exercise, int setNumber, int reps, BigDecimal weightKg, Integer rpe) {
        this.exercise = exercise;
        this.setNumber = setNumber;
        this.reps = reps;
        this.weightKg = weightKg;
        this.rpe = rpe;
    }

    /**
     * Estimated one-rep max using the Epley formula: {@code w * (1 + reps/30)}.
     * For a single rep this collapses to the load itself. Only meaningful for
     * strength movements; callers should filter by {@link ExerciseCategory}.
     */
    public BigDecimal estimatedOneRepMax() {
        BigDecimal factor = BigDecimal.ONE.add(
                BigDecimal.valueOf(reps).divide(BigDecimal.valueOf(30), 6, java.math.RoundingMode.HALF_UP));
        return weightKg.multiply(factor).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /** Training volume of this set: reps × load. */
    public BigDecimal volume() {
        return weightKg.multiply(BigDecimal.valueOf(reps));
    }

    public Long getId() {
        return id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public Integer getRpe() {
        return rpe;
    }

    public void setRpe(Integer rpe) {
        this.rpe = rpe;
    }
}
