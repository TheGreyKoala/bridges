/**
 * @author Tim Gremplewski
 */
public class PuzzleBuilder {

    private Puzzle puzzle;

    public static PuzzleBuilder initPuzzle(final int width, final int height) {
        PuzzleBuilder puzzleBuilder = new PuzzleBuilder();
        puzzleBuilder.puzzle = new PuzzleImpl(width, height);
        return puzzleBuilder;
    }

    public PuzzleBuilder addIsland(final int xPosition, final int yPosition, final int requiredBridges) {
        // TODO implement addIsland
        return this;
    }

    public PuzzleBuilder addBridge(final Island islandA, final Island islandB) {
        // TODO implement addBridge
        return this;
    }

    public Puzzle getResult() {
        return puzzle;
    }
}
