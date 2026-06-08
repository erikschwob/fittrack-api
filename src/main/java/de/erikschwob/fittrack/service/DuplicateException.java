package de.erikschwob.fittrack.service;

/** Thrown when creating an entity that violates a uniqueness rule. Mapped to HTTP 409. */
public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }
}
