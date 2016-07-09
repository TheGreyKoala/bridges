package de.feu.ps.bridges.toolkit;

/**
 * General execption that indicates an error during a puzzle operation.
 * @author Tim Gremplewski
 */
public class GeneralException extends RuntimeException {

    /**
     * Create a new instance.
     * @param message the error message.
     * @param cause the exception cause.
     */
    public GeneralException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
