package de.erikschwob.fittrack.repository;

import de.erikschwob.fittrack.model.BodyMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BodyMetricRepository extends JpaRepository<BodyMetric, Long> {

    Optional<BodyMetric> findByMeasuredOn(LocalDate measuredOn);
}
