package de.feu.ps.bridges.serialization;

import de.feu.ps.bridges.model.Bridge;
import de.feu.ps.bridges.model.Island;
import de.feu.ps.bridges.model.Puzzle;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;

/**
 * @author Tim Gremplewski
 */
public class Serializer {

    public void storePuzzle(Puzzle puzzle, String destination) throws FileNotFoundException {

        // TODO throw runtime exception -> Also see BridgesTesterImpl

        // TODO I think the file must not exist and be created?

        if (!destination.endsWith(".bgs")) {
            destination += ".bgs";
        }

        Set<Island> islands = puzzle.getIslands();
        Set<Bridge> bridges = puzzle.getBridges();

        try (PrintWriter writer = new PrintWriter(destination)) {
            writer.println(Keyword.FIELD.name());
            writer.print(puzzle.getColumnsCount());
            writer.print(" x ");
            writer.print(puzzle.getRowsCount());
            writer.print(" | ");

            // TODO Implement seperate method for islands count?
            writer.println(islands.size());
            writer.println();
            writer.println(Keyword.ISLANDS.name());

            Stream<Island> sortedIslands = islands.stream()
                    // TODO THIS NEEDS TO BE TESTED!
                    .sorted(comparingInt(Island::getColumnIndex).thenComparing(Island::getRowIndex));

            List<Island> islandList = sortedIslands.collect(Collectors.toList());

            islandList.forEach(island -> {
                        writer.print("( ");
                        writer.print(island.getColumnIndex());
                        writer.print(", ");
                        writer.print(island.getRowIndex());
                        writer.print(" | ");
                        writer.print(island.getRequiredBridges());
                        writer.println(" )");
                    });

            writer.println();

            if (!bridges.isEmpty()) {
                writer.println(Keyword.BRIDGES.name());

                bridges.stream().sorted(Comparator.<Bridge>comparingInt(value -> {
                    int island1Index = islandList.indexOf(value.getIsland1());
                    int island2Index = islandList.indexOf(value.getIsland2());
                    return Math.min(island1Index, island2Index);
                }).thenComparingInt(value -> {
                    int island1Index = islandList.indexOf(value.getIsland1());
                    int island2Index = islandList.indexOf(value.getIsland2());
                    return Math.max(island1Index, island2Index);
                })).forEach(bridge -> {
                    int island1Index = islandList.indexOf(bridge.getIsland1());
                    int island2Index = islandList.indexOf(bridge.getIsland2());

                    writer.print("( ");
                    writer.print(Math.min(island1Index, island2Index));
                    writer.print(", ");
                    writer.print(Math.max(island1Index, island2Index));
                    writer.print(" | ");
                    writer.print(bridge.isDoubleBridge());
                    writer.println(" )");
                });
            }
        }
    }
}
