package de.erikschwob.fittrack.api;

import de.erikschwob.fittrack.api.dto.WorkoutDtos.AddSetRequest;
import de.erikschwob.fittrack.api.dto.WorkoutDtos.CreateWorkoutRequest;
import de.erikschwob.fittrack.api.dto.WorkoutDtos.SetResponse;
import de.erikschwob.fittrack.api.dto.WorkoutDtos.WorkoutResponse;
import de.erikschwob.fittrack.model.Workout;
import de.erikschwob.fittrack.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Workouts", description = "Training sessions and the sets logged within them")
public class WorkoutController {

    private final WorkoutService service;

    public WorkoutController(WorkoutService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List workouts", description = "Optionally bounded by an inclusive date range")
    public List<WorkoutResponse> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.find(from, to).stream().map(WorkoutResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a workout with all its sets")
    public WorkoutResponse get(@PathVariable Long id) {
        return WorkoutResponse.from(service.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a workout")
    public WorkoutResponse create(@Valid @RequestBody CreateWorkoutRequest request) {
        Workout workout = service.create(request.performedOn(), request.title(), request.notes());
        return WorkoutResponse.from(workout);
    }

    @PostMapping("/{id}/sets")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Append a set to a workout",
            description = "The set number is assigned automatically as the next position in the session")
    public SetResponse addSet(@PathVariable Long id, @Valid @RequestBody AddSetRequest request) {
        return SetResponse.from(
                service.addSet(id, request.exerciseId(), request.reps(), request.weightKg(), request.rpe()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a workout and its sets")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
