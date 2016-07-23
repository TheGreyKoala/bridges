package de.feu.ps.bridges.toolkit;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Direction;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.io.File;
import java.util.Optional;

/**
 * Interface for a helper class hat aggregates common tasks regarding {@link Puzzle}s.
 * @author Tim Gremplewski
 */
public interface PuzzleToolkit {

    /**
     * Get the puzzle of this instance.
     * @return the puzzle of this instance.
     */
    Puzzle getPuzzle();

    /**
     * Get the current status of the puzzle.
     * @return the current status of the puzzle.
     */
    PuzzleStatus getPuzzleStatus();

    /**
     * Apply a next save move on the puzzle.
     * @return {@link Optional} containing the {@link Bridge} that was created when the move was applied,
     *  if any could be found.
     * @throws GeneralException if anything goes wrong.
     */
    Optional<Bridge> nextMove();

    /**
     * Remove a bridge in the given direction from the given island.
     * @param island Island from which the bridge should be removed.
     * @param direction Direction that specifies the bridge that should be removed.
     * @return {@link Optional} containing the {@link Bridge} that was removed, if any.
     * @throws GeneralException if anything goes wrong.
     */
    Optional<Bridge> tearDownBridge(Island island, Direction direction);

    /**
     * Save the puzzle at the given destination.
     * @param destinationFile Location where the puzzle should be saved.
     * @throws GeneralException if anything goes wrong.
     */
    void savePuzzle(final File destinationFile);

    /**
     * Solve the puzzle.
     * @throws GeneralException if anything goes wrong.
     */
    void solvePuzzle();

    /**
     * Try to add a bridge in the given direction from the given island.
     * If such a bridge is not valid, the returned {@link Optional} will be empty.
     *
     * @param island Island to which the bridge should be added.
     * @param direction Direction of the new bridge.
     * @return {@link Optional} containing the new {@link Bridge}, if one was created.
     * @throws GeneralException if anything goes wrong.
     */
    Optional<Bridge> tryBuildBridge(Island island, Direction direction);
}
