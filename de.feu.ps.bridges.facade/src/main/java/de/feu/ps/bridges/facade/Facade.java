package de.feu.ps.bridges.facade;

import de.feu.ps.bridges.model.Puzzle;
import de.feu.ps.bridges.serialization.Deserializer;

import java.io.File;

/**
 * @author Tim Gremplewski
 */
public class Facade {

    public static Puzzle loadPuzzle(File sourceFile) {
        Deserializer deserializer = new Deserializer();
        try {
            return deserializer.loadPuzzle(sourceFile.getPath());
        } catch (Exception e) {
            // TODO exception handling!
            e.printStackTrace();
            return null;
        }
    }
}
