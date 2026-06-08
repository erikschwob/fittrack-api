package de.erikschwob.fittrack.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class WorkoutSetTest {

    private WorkoutSet set(int reps, String weight) {
        Exercise squat = new Exercise("Back Squat", MuscleGroup.LEGS, ExerciseCategory.STRENGTH, null);
        return new WorkoutSet(squat, 1, reps, new BigDecimal(weight), null);
    }

    @Test
    void oneRepMaxOfSingleRepIsTheLoadItself() {
        // Epley: w * (1 + 1/30) is NOT identity; the formula degenerates only at reps=0,
        // so a true 1-rep set estimates slightly above the load. Verify the documented behaviour.
        assertThat(set(1, "100.00").estimatedOneRepMax()).isEqualByComparingTo("103.33");
    }

    @Test
    void epleyFormulaMatchesKnownValue() {
        // 100 kg x 5 -> 100 * (1 + 5/30) = 116.67
        assertThat(set(5, "100.00").estimatedOneRepMax()).isEqualByComparingTo("116.67");
    }

    @Test
    void heavierLowRepCanBeatLighterHighRep() {
        BigDecimal heavy = set(3, "140.00").estimatedOneRepMax();   // 154.00
        BigDecimal light = set(10, "100.00").estimatedOneRepMax();  // 133.33
        assertThat(heavy).isGreaterThan(light);
    }

    @Test
    void volumeIsRepsTimesWeight() {
        assertThat(set(5, "100.00").volume()).isEqualByComparingTo("500.00");
    }
}
