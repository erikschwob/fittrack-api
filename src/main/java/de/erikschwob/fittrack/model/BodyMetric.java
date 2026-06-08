package de.erikschwob.fittrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A body measurement taken on a given day. Tracked alongside training so that
 * strength progress can be read in the context of bodyweight changes.
 */
@Entity
@Table(name = "body_metric")
public class BodyMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "measured_on", nullable = false, unique = true)
    private LocalDate measuredOn;

    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "body_fat_pct", precision = 4, scale = 1)
    private BigDecimal bodyFatPct;

    @Column(name = "resting_hr")
    private Integer restingHr;

    protected BodyMetric() {
        // for JPA
    }

    public BodyMetric(LocalDate measuredOn, BigDecimal weightKg, BigDecimal bodyFatPct, Integer restingHr) {
        this.measuredOn = measuredOn;
        this.weightKg = weightKg;
        this.bodyFatPct = bodyFatPct;
        this.restingHr = restingHr;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getMeasuredOn() {
        return measuredOn;
    }

    public void setMeasuredOn(LocalDate measuredOn) {
        this.measuredOn = measuredOn;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getBodyFatPct() {
        return bodyFatPct;
    }

    public void setBodyFatPct(BigDecimal bodyFatPct) {
        this.bodyFatPct = bodyFatPct;
    }

    public Integer getRestingHr() {
        return restingHr;
    }

    public void setRestingHr(Integer restingHr) {
        this.restingHr = restingHr;
    }
}
