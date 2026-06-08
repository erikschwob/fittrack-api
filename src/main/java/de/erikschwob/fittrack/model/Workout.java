package de.erikschwob.fittrack.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A single training session on a given day. Owns its {@link WorkoutSet}s:
 * deleting a workout removes its sets (cascade + orphan removal).
 */
@Entity
@Table(name = "workout")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "performed_on", nullable = false)
    private LocalDate performedOn;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setNumber ASC")
    private List<WorkoutSet> sets = new ArrayList<>();

    protected Workout() {
        // for JPA
    }

    public Workout(LocalDate performedOn, String title, String notes) {
        this.performedOn = performedOn;
        this.title = title;
        this.notes = notes;
    }

    /**
     * Adds a set and keeps both sides of the bidirectional association in sync.
     */
    public void addSet(WorkoutSet set) {
        set.setWorkout(this);
        this.sets.add(set);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPerformedOn() {
        return performedOn;
    }

    public void setPerformedOn(LocalDate performedOn) {
        this.performedOn = performedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<WorkoutSet> getSets() {
        return sets;
    }
}
