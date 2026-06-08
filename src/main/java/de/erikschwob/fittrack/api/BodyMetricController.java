package de.erikschwob.fittrack.api;

import de.erikschwob.fittrack.api.dto.BodyMetricDtos.BodyMetricResponse;
import de.erikschwob.fittrack.api.dto.BodyMetricDtos.CreateBodyMetricRequest;
import de.erikschwob.fittrack.service.BodyMetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/body-metrics")
@Tag(name = "Body metrics", description = "Bodyweight and related daily measurements")
public class BodyMetricController {

    private final BodyMetricService service;

    public BodyMetricController(BodyMetricService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List body metrics, newest first")
    public List<BodyMetricResponse> list() {
        return service.findAll().stream().map(BodyMetricResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Record a body metric", description = "At most one entry per day")
    public BodyMetricResponse create(@Valid @RequestBody CreateBodyMetricRequest request) {
        return BodyMetricResponse.from(service.record(request.toEntity()));
    }
}
