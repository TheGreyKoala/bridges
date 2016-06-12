package de.feu.ps.bridges.solver;

import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Deserializer;
import org.junit.Test;

/**
 * @author Tim Gremplewski
 */
public class SolverTest {
    @Test
    public void solve() throws Exception {
        final String filePath = getClass().getResource("bsp_5x5.bgs").toURI().getPath();

        Deserializer deserializer = new Deserializer();
        Puzzle puzzle = deserializer.loadPuzzle(filePath);

        Solver solver = new Solver();
        solver.solve(puzzle);
    }

    @Test
    public void getSafeMove() throws Exception {

    }

}