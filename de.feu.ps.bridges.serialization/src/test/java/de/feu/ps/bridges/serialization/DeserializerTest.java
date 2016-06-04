package de.feu.ps.bridges.serialization;

import org.junit.Test;

/**
 * @author Tim Gremplewski
 */
public class DeserializerTest {

    @Test
    public void loadPuzzle() throws Exception {
        final String filePath = getClass().getResource("bsp_abb2_loesung.bgs").toURI().getPath();

        Deserializer deserializer = new Deserializer();
        deserializer.loadPuzzle(filePath);
    }
}