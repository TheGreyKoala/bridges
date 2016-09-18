package de.feu.ps.bridges.generator;

import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Position;
import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.model.PuzzleBuilder;

/**
 * @author Tim Gremplewski
 */
public class PuzzleComposer {

    private final Puzzle puzzle1;
    private final Puzzle puzzle2;

    public PuzzleComposer(final Puzzle puzzle1, final Puzzle puzzle2) {
        this.puzzle1 = puzzle1;
        this.puzzle2 = puzzle2;
    }

    public Puzzle compose() {
        int columnsCount = puzzle1.getColumnsCount() + puzzle2.getColumnsCount() + 1;
        int rowsCount = puzzle1.getRowsCount() + puzzle2.getRowsCount() + 1;
        int islandsCount = puzzle1.getIslands().size() + puzzle2.getIslands().size();

        PuzzleBuilder puzzleBuilder = PuzzleBuilder.createBuilder(columnsCount, rowsCount, islandsCount);

        Island rightAndUppermostIsland = puzzle1.getRightAndUppermostIsland();
        Island leftAndUppermostIsland = puzzle2.getLeftAndUppermostIsland();

        int leftPuzzleRowOffset = 0;
        int rightPuzzleRowOffset = 0;
        int rightPuzzleColumnOffset = puzzle1.getColumnsCount() + 1;

        if (rightAndUppermostIsland.getPosition().getRow() < leftAndUppermostIsland.getPosition().getRow()) {
            leftPuzzleRowOffset = leftAndUppermostIsland.getPosition().getRow() - rightAndUppermostIsland.getPosition().getRow();
        } else {
            rightPuzzleRowOffset = rightAndUppermostIsland.getPosition().getRow() - leftAndUppermostIsland.getPosition().getRow();
        }

        for (Island island : puzzle1.getIslands()) {
            Position position = new Position(island.getPosition().getColumn(), island.getPosition().getRow() + leftPuzzleRowOffset);

            if (island == rightAndUppermostIsland) {
                puzzleBuilder.addIsland(position, island.getRequiredBridges() + 1);
            } else {
                puzzleBuilder.addIsland(position, island.getRequiredBridges());
            }
        }

        for (Island island : puzzle2.getIslands()) {
            Position position = new Position(island.getPosition().getColumn() + rightPuzzleColumnOffset, island.getPosition().getRow() + rightPuzzleRowOffset);

            if (island == leftAndUppermostIsland) {
                puzzleBuilder.addIsland(position, island.getRequiredBridges() + 1);
            } else {
                puzzleBuilder.addIsland(position, island.getRequiredBridges());
            }
        }

        return puzzleBuilder.getResult();
    }
}
