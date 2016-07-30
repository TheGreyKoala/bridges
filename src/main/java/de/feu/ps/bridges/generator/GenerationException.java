package de.feu.ps.bridges.generator;

/**
 * {@link RuntimeException} that indicates an error during the generation of a puzzle.
 * @author Tim Gremplewski
 */
class GenerationException extends RuntimeException {

    /**
     * Create a new exception.
     * @param message Message of the exception.
     */
    GenerationException(String message) {
        super(message);
    }

    /**
     * Create a new exception.
     * @param message Message of the exception.
     * @param cause Cause of the exception.
     */
    GenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
