package de.feu.ps.bridges.generator;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;

/**
 * Helper class that offers utility methods that are helpful for the random generation of a puzzle.
 * @author Tim Gremplewski
 */
final class RandomUtil {

    private final Random random;

    /**
     * Create a new instance.
     */
    RandomUtil() {
        random = new Random();
    }

    /**
     * Get the {@link Random} object used by this instance.
     * @return the {@link Random} object used by this instance.
     */
    Random getRandom() {
        return random;
    }

    /**
     * Return a randomly selected item from the given list.
     * @param <T> Type of the items in the list.
     * @param list List of items of type T.
     * @return a randomly selected item from the given list.
     * @
     */
    <T> T pickRandomFrom(final List<T> list) {
        final int index = randomIntBetweenZeroAnd(list.size() - 1);
        return list.get(index);
    }

    /**
     * Get a random integer between the given bounds (inclusively).
     * @param lowerLimit Lower limit of the random int.
     * @param upperLimit Upper limit of the random int.
     * @return a random integer between the given bounds (inclusively).
     */
    int randomIntBetween(final int lowerLimit, final int upperLimit) {
        if (lowerLimit == upperLimit) {
            return lowerLimit;
        }

        OptionalInt optionalInt = random.ints(1, lowerLimit, upperLimit + 1).findFirst();
        if (optionalInt.isPresent()) {
            return optionalInt.getAsInt();
        } else {
            throw new IllegalArgumentException("Could not find a random int between the given boundaries.");
        }
    }

    /**
     * Get a random integer between 0 and the given bound (inclusively).
     * @param bound bound of the random integer.
     * @return a random integer between 0 and the given bound (inclusively).
     */
    int randomIntBetweenZeroAnd(int bound) {
        return random.nextInt(bound + 1);
    }
}
