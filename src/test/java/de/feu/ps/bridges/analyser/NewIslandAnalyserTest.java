package de.feu.ps.bridges.analyser;

import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.model.PuzzleBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Gremplewski
 */
public class NewIslandAnalyserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private NewIslandAnalyser analyser;

    @Before
    public void setUp() throws Exception {
        PuzzleBuilder puzzleBuilder = PuzzleBuilder.createBuilder(10, 10, 5);
        puzzleBuilder.addIsland(new Position(5, 5), 5);
        Puzzle puzzle = puzzleBuilder.getResult();
        analyser = new NewIslandAnalyser(puzzle, new MoveAnalyser(puzzle));
    }

    @Test
    public void testPuzzleNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'puzzle' must not be null."));
        new NewIslandAnalyser(null, new MoveAnalyser(PuzzleBuilder.createBuilder(10, 10, 5).getResult()));
    }

    @Test
    public void testMoveAnalyserNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("Parameter 'moveAnalyser' must not be null."));
        PuzzleBuilder puzzleBuilder = PuzzleBuilder.createBuilder(10, 10, 5);
        puzzleBuilder.addIsland(new Position(5, 5), 5);
        Puzzle puzzle = puzzleBuilder.getResult();
        analyser = new NewIslandAnalyser(puzzle, null);
    }

    @Test
    public void testIsValidIslandPositionColumnOutsidePuzzle() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(11, 8));
        assertFalse("Expected position to be invalid.", validIslandPosition);
    }

    @Test
    public void testIsValidIslandPositionRowOutsidePuzzle() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(8, 11));
        assertFalse("Expected position to be invalid.", validIslandPosition);
    }

    @Test
    public void testIsValidIslandPositionNorthAdjacentIsland() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(5, 6));
        assertFalse("Expected position to be invalid.", validIslandPosition);
    }

    @Test
    public void testIsValidIslandPositionEastAdjacentIsland() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(4, 5));
        assertFalse("Expected position to be invalid.", validIslandPosition);
    }

    @Test
    public void testIsValidIslandPositionSouthAdjacentIsland() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(5, 4));
        assertFalse("Expected position to be invalid.", validIslandPosition);
    }

    @Test
    public void testIsValidIslandPositionWestAdjacentIsland() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(6, 5));
        assertFalse("Expected position to be invalid.", validIslandPosition);
    }

    @Test
    public void testIsValidIslandPosition() {
        boolean validIslandPosition = analyser.isValidIslandPosition(new Position(8, 7));
        assertTrue("Expected position to be valid.", validIslandPosition);
    }
}