package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.model.Puzzle;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Tim Gremplewski
 */
public class PuzzleGeneratorTest {

    @Test
    public void create() throws Exception {
        Puzzle puzzle = PuzzleGeneratorImpl.generatePuzzle(15, 20, 48);
        assertNotNull(puzzle);
    }
}