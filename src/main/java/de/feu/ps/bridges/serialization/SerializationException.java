package de.feu.ps.bridges.serialization;

/**
 * {@link RuntimeException} that indicates and error during the serialization or deserialization of a puzzle.
 * @author Tim Gremplewski
 */
class SerializationException extends RuntimeException {

    /**
     * Create a new exception.
     * @param message Message of the exception.
     * @param cause Cause of the exception.
     */
    SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
