package de.feu.ps.bridges.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class PuzzleImpl implements Puzzle {

    private final int width;
    private final int height;

    private final Set<Island> islands;
    private final Set<Bridge> bridges;

    public PuzzleImpl(final int width, final int height) {

        //TODO Parameter validation

        this.width = width;
        this.height = height;
        islands = new HashSet<>();
        bridges = new HashSet<>();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Set<Island> getIslands() {
        return new HashSet<>(islands);
    }

    @Override
    public Set<Bridge> getBridges() {
        return new HashSet<>(bridges);
    }

    @Override
    public PuzzleStatus getStatus() {
        //TODO Implement de.feu.ps.bridges.model.Puzzle.getStatus
        return null;
    }
}
