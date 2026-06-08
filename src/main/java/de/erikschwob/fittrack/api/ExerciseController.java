package de.erikschwob.fittrack.api;

import de.erikschwob.fittrack.api.dto.ExerciseDtos.CreateExerciseRequest;
import de.erikschwob.fittrack.api.dto.ExerciseDtos.ExerciseResponse;
import de.erikschwob.fittrack.model.MuscleGroup;
import de.erikschwob.fittrack.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@Tag(name = "Exercises", description = "The exercise catalogue")
public class ExerciseController {

    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List exercises", description = "Optionally filtered by muscle group")
    public List<ExerciseResponse> list(@RequestParam(required = false) MuscleGroup muscleGroup) {
        return service.findAll(muscleGroup).stream().map(ExerciseResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single exercise by id")
    public ExerciseResponse get(@PathVariable Long id) {
        return ExerciseResponse.from(service.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an exercise")
    public ResponseEntity<ExerciseResponse> create(@Valid @RequestBody CreateExerciseRequest request) {
        ExerciseResponse body = ExerciseResponse.from(service.create(request.toEntity()));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
