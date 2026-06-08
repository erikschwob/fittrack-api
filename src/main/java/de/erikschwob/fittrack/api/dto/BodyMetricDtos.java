package de.erikschwob.fittrack.api.dto;

import de.erikschwob.fittrack.model.BodyMetric;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Request/response DTOs for body measurements. */
public final class BodyMetricDtos {

    private BodyMetricDtos() {
    }

    public record CreateBodyMetricRequest(
            @NotNull LocalDate measuredOn,
            @NotNull @DecimalMin("20.0") BigDecimal weightKg,
            @DecimalMin("0.0") BigDecimal bodyFatPct,
            @Positive Integer restingHr) {

        public BodyMetric toEntity() {
            return new BodyMetric(measuredOn, weightKg, bodyFatPct, restingHr);
        }
    }

    public record BodyMetricResponse(
            Long id,
            LocalDate measuredOn,
            BigDecimal weightKg,
            BigDecimal bodyFatPct,
            Integer restingHr) {

        public static BodyMetricResponse from(BodyMetric m) {
            return new BodyMetricResponse(m.getId(), m.getMeasuredOn(), m.getWeightKg(), m.getBodyFatPct(),
                    m.getRestingHr());
        }
    }
}
