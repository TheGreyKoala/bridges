package de.feu.ps.bridges.analyser;

/**
 * @author Tim Gremplewski
 */
public enum PuzzleStatus {

    /**
     * The puzzle is completely solved.
     * <ul>
     *     <li>All islands have the required amount of bridges</li>
     *     <li>Every islands is at least indirectly connected to every other island</li>
     *     <li>No bridge violates any rule</li>
     * </ul>
     */
    SOLVED,

    /**
     * The puzzle is not completely solved, i.e. does not fulfill all conditions
     * of the status {@link #SOLVED}, but at least one valid move is still possible.
     */
    UNSOLVED,

    /**
     * Same as {@link #UNSOLVED}, but no valid move is possible.
     */
    UNSOLVABLE
}
