package de.erikschwob.fittrack.service;

import de.erikschwob.fittrack.model.BodyMetric;
import de.erikschwob.fittrack.repository.BodyMetricRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BodyMetricService {

    private final BodyMetricRepository metrics;

    public BodyMetricService(BodyMetricRepository metrics) {
        this.metrics = metrics;
    }

    @Transactional(readOnly = true)
    public List<BodyMetric> findAll() {
        return metrics.findAll(org.springframework.data.domain.Sort.by("measuredOn").descending());
    }

    /**
     * Records a measurement. At most one entry per day: a second record for the
     * same date is rejected rather than silently overwriting the first.
     */
    public BodyMetric record(BodyMetric metric) {
        metrics.findByMeasuredOn(metric.getMeasuredOn()).ifPresent(existing -> {
            throw new DuplicateException("A body metric for " + metric.getMeasuredOn() + " already exists");
        });
        return metrics.save(metric);
    }
}
