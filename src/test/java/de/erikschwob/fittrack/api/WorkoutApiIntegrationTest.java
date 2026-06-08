package de.erikschwob.fittrack.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end test of the full happy path: create an exercise, log a workout with
 * two sets, then read back the personal record and muscle-group volume the API
 * derived from them. Runs against H2 with the real Flyway migrations.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkoutApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @Test
    void logsWorkoutAndDerivesStats() throws Exception {
        long exerciseId = createExercise("""
                {"name":"Test Squat","muscleGroup":"LEGS","category":"STRENGTH","description":"int-test"}""");

        long workoutId = createWorkout("""
                {"performedOn":"2026-06-01","title":"Leg day","notes":"felt strong"}""");

        // 5 x 100 kg  -> est. 1RM 116.67 ; volume 500
        addSet(workoutId, """
                {"exerciseId":%d,"reps":5,"weightKg":100.0,"rpe":8}""".formatted(exerciseId));
        // 3 x 140 kg  -> est. 1RM 154.00 ; volume 420  (this is the PR)
        addSet(workoutId, """
                {"exerciseId":%d,"reps":3,"weightKg":140.0,"rpe":9}""".formatted(exerciseId));

        // workout now carries both sets, numbered automatically
        mvc.perform(get("/api/workouts/{id}", workoutId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sets.length()").value(2))
                .andExpect(jsonPath("$.sets[0].setNumber").value(1))
                .andExpect(jsonPath("$.sets[1].setNumber").value(2));

        // personal record = best estimated 1RM across the two sets
        mvc.perform(get("/api/stats/personal-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.exerciseName=='Test Squat')].estimatedOneRepMax").value(154.00));

        // total leg volume = 500 + 420 = 920
        mvc.perform(get("/api/stats/volume")
                        .param("from", "2026-06-01").param("to", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.muscleGroup=='LEGS')].volume").value(920.00));
    }

    @Test
    void rejectsDuplicateExerciseWith409() throws Exception {
        createExercise("""
                {"name":"Unique Lift","muscleGroup":"BACK","category":"STRENGTH"}""");
        mvc.perform(post("/api/exercises").contentType(MediaType.APPLICATION_JSON).content("""
                        {"name":"unique lift","muscleGroup":"BACK","category":"STRENGTH"}"""))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsInvalidExerciseWith400() throws Exception {
        mvc.perform(post("/api/exercises").contentType(MediaType.APPLICATION_JSON).content("""
                        {"name":"","muscleGroup":"BACK","category":"STRENGTH"}"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returns404ForUnknownWorkout() throws Exception {
        mvc.perform(get("/api/workouts/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsSecondBodyMetricForSameDayWith409() throws Exception {
        mvc.perform(post("/api/body-metrics").contentType(MediaType.APPLICATION_JSON).content("""
                        {"measuredOn":"2026-05-01","weightKg":82.5,"bodyFatPct":15.2,"restingHr":54}"""))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/body-metrics").contentType(MediaType.APPLICATION_JSON).content("""
                        {"measuredOn":"2026-05-01","weightKg":82.0}"""))
                .andExpect(status().isConflict());
    }

    @Test
    void seedExercisesAreAvailable() throws Exception {
        mvc.perform(get("/api/exercises").param("muscleGroup", "CHEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name=='Bench Press')]").exists());
    }

    private long createExercise(String body) throws Exception {
        MvcResult r = mvc.perform(post("/api/exercises").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn();
        return idOf(r);
    }

    private long createWorkout(String body) throws Exception {
        MvcResult r = mvc.perform(post("/api/workouts").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn();
        return idOf(r);
    }

    private void addSet(long workoutId, String body) throws Exception {
        mvc.perform(post("/api/workouts/{id}/sets", workoutId).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
    }

    private long idOf(MvcResult result) throws Exception {
        JsonNode node = json.readTree(result.getResponse().getContentAsString());
        assertThat(node.has("id")).isTrue();
        return node.get("id").asLong();
    }
}
