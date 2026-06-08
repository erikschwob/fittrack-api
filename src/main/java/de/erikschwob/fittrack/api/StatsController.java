package de.erikschwob.fittrack.api;

import de.erikschwob.fittrack.service.MuscleVolume;
import de.erikschwob.fittrack.service.PersonalRecord;
import de.erikschwob.fittrack.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@Tag(name = "Stats", description = "Analytics derived from logged sets")
public class StatsController {

    private final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @GetMapping("/personal-records")
    @Operation(summary = "Best estimated one-rep max per strength exercise",
            description = "Estimated via the Epley formula: weight × (1 + reps/30)")
    public List<PersonalRecord> personalRecords() {
        return service.personalRecords();
    }

    @GetMapping("/volume")
    @Operation(summary = "Training volume per muscle group",
            description = "Sum of weight × reps over an inclusive date range, grouped by muscle group")
    public List<MuscleVolume> volume(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.volumeByMuscleGroup(from, to);
    }
}
