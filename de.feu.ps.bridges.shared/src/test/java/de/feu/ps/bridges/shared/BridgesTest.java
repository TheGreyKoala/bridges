package de.feu.ps.bridges.shared;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Diese Klasse stellt eine abstrakte Klasse dar, mit deren Hilfe die
 * Korrektheit des Algorithmus zum Lösen eines Bridges-Rätsels überprüft werden
 * kann.
 *
 * @author ProPra - FernUniversität Hagen
 */
public abstract class BridgesTest {

    /** Verzeichnis für die Eingabedateien */
    private final static String DATA_DIR = getDataDir();

    private static String getDataDir() {
        try {
            return BridgesTest.class.getResource("data").toURI().getPath();
        } catch (URISyntaxException e) {
            return "";
        }
    }

    /** Dateierweiterung für eine Rätseldatei (Eingabedatei) */
    private final static String EXT_PUZZLE = "bgs";

    /** Dateierweiterung für die Vergleichsdatei (vorgegebene Lösung) */
    private final static String EXT_SOLUTION = "sol";

    /** Dateierweiterung für die berechnete Lösung des Rätsels */
    private final static String EXT_CALCULATED = "calc";

    /** String für die Angabe Name, Matrikel-Nr. und E-Mailadresse des Studenten */
    private static String strNameMatrikelnummer = null;

    /** Startzeit für den gesamten Testlauf */
    private static long startTime;

    /** für die Zusammenfassung der Testergebnisse */
    private static StringBuffer sbSummary = new StringBuffer();

    /** Zähler für alle Testfälle */
    private static int counter = 0;

    /** Zähler für fehlgeschlagene Testfälle */
    private static int counterFailed = 0;

    /** Eingabedateiname */
    private String inputFilename;

    /**
     * Die Methode liefert eine Implementierung des Interfaces BridgesTester.
     *
     * @return eine Implementierung des Interfaces BridgesTester
     */
    public abstract BridgesTester getBridgesTesterImpl();

    /**
     * Gibt die Matrikelnummer des bearbeitenden Studenten zurück.
     *
     * @return die Matrikelnummer des Studenten
     */
    public abstract String getMatrNr();

    /**
     * Gibt den kompletten Namen des bearbeitenden Studenten zurück.
     *
     * @return der Name des Studenten
     */
    public abstract String getName();

    /**
     * Gibt die E-Mail des bearbeitenden Studenten zurück.
     *
     * @return die E-Mail des Studenten
     */
    public abstract String getEmail();

    /**
     * Dies ist eine Hilfsmethode.
     *
     * <p>
     * Die Methode konvertiert den Inhalt einer Datei mit dem angegebenen
     * Dateinamen in einen String und liefert diesen als Rückgabewert. Dabei
     * werden Kommentarzeilen (Zeilen, die mit "#" beginnen) sowie alle
     * "Whitespaces" ausgefiltert.
     * </p>
     *
     * @param filename
     *            Dateiname der Datei, deren Inhalt konvertiert wird
     * @return der konvertierte Inhalt der Datei als ein String
     * @throws IOException
     *             wenn bei der Dateiverarbeitung eine IOException auftritt
     */
    private static String convertFileToString(String filename)
            throws IOException {
        // Rückgabewert
        StringBuffer sb = new StringBuffer();

        BufferedReader in = new BufferedReader(new FileReader(filename));

        String line = in.readLine();
        while (line != null) {
            if (!line.startsWith("#")) {
                sb.append(line);
                sb.append("\r\n");
            }
            line = in.readLine();
        }

        in.close();

        String result = sb.toString();
        result = result.replaceAll("\\s+", "");

        return result.toString();
    }

    /**
     * Dies ist die Test-Methode, die zu einem Rätsel die berechnete Lösung mit
     * der erwarteten Lösung vergleicht.
     *
     * @param puzzle
     *            Name des Rätsels (entspricht dem Dateinamen ohne
     *            Dateierweiterung
     * @throws IOException
     */
    public void doTest(String puzzle) throws IOException {
        // Der Eingabedateiname wird für das Testergebnis benötigt.
        inputFilename = puzzle + "." + EXT_PUZZLE;

        // Zusammenbauen der Dateinamen (Eingabedatei, Vergleichsdatei,
        // Ausgabedatei)
        String filenamePuzzle = DATA_DIR + File.separator + puzzle + "."
                + EXT_PUZZLE;
        String filenameExpected = DATA_DIR + File.separator + puzzle + "."
                + EXT_SOLUTION + "." + EXT_PUZZLE;
        String filenameCalculated = DATA_DIR + File.separator + puzzle + "."
                + EXT_CALCULATED + "." + EXT_PUZZLE;

        // Eventuell existierende Datei (filenameCalculated) löschen.
        if (Files.exists(Paths.get(filenameCalculated))) {
            Files.delete(Paths.get(filenameCalculated));
        }

        // Aufruf der Testmethode
        BridgesTester tester = getBridgesTesterImpl();
        tester.testSolvePuzzle(filenamePuzzle, filenameCalculated);

        // Die beiden Dateien (erwartete Lösung und berechnete Lösung) werden
        // in Strings umgewandelt, die dann auf Gleichheit überprüft werden.
        String expectedSolution = convertFileToString(filenameExpected);
        String calculatedSolution = convertFileToString(filenameCalculated);
        assertEquals(expectedSolution, calculatedSolution);
    }

    @BeforeClass
    public static void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    @AfterClass
    public static void printSummary() {
        double seconds = (System.currentTimeMillis() - startTime) / 1000.0;

        StringBuffer sb = new StringBuffer();

        // Ausgabe einer Zusammenfassung des Testergebnisses auf der Konsole

        sb.append("========================================================================================================\n");
        sb.append("Bridges Test - Zusammenfassung für \n");
        sb.append(strNameMatrikelnummer + "\n");
        sb.append("========================================================================================================\n");

        String str = String.format(
                "| %-15.15s | %-25s | %-25s | %-9s | %14s | %n", "Klasse",
                "Methode", "Eingabedatei", "Erfolg", "benötigte Zeit");
        sb.append(str);

        sb.append("========================================================================================================\n");

        sb.append(sbSummary);

        sb.append("========================================================================================================\n");

        sb.append(counter + " Test(s) bearbeitet, " + (counter - counterFailed)
                + " Test(s) bestanden\n");
        str = String.format("Benötigte Zeit: %.3f s\n", seconds);
        sb.append(str);

        if (counterFailed > 0) {
            sb.append("\nHinweis: bei fehlerhaften Testfällen können Sie "
                    + "die entsprechenden Dateien \"*." + EXT_SOLUTION + "."
                    + EXT_PUZZLE + "\" " + "und \"*." + EXT_CALCULATED + "."
                    + EXT_PUZZLE + "\"\n" + "im Verzeichnis \"" + DATA_DIR
                    + "\"" + " vergleichen.");
        }

        System.out.println(sb);
    }

    @Rule
    public TestWatcher testResultWatcher = new TestWatcher() {
        long timeStart;
        String result;
        String message;

        @Override
        protected void starting(Description description) {
            counter++;
            if (strNameMatrikelnummer == null) {
                strNameMatrikelnummer = getName() + ", " + getMatrNr() + ", "
                        + getEmail();
            }
            timeStart = System.currentTimeMillis();
        }

        @Override
        protected void finished(Description description) {
            double seconds = (System.currentTimeMillis() - timeStart) / 1000.0;

            String line = String.format(
                    "| %-15.15s | %-25s | %-25s | %-9s | %12.3f s | %s%n",
                    description.getClassName(), description.getMethodName(),
                    inputFilename, result, seconds, message);
            sbSummary.append(line);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            counterFailed++;
            result = "failed";
            message = e.getMessage();
        }

        @Override
        protected void succeeded(Description description) {
            result = "succeeded";
            message = "";
        }
    };

    @Test
    public void test_Beispiel_Abb2() throws IOException {
        doTest("bsp_abb2");
    }

    @Test
    public void test_Beispiel_Abb22() throws IOException {
        doTest("bsp_abb22");
    }

    @Test
    public void test_Beispiel_5x5() throws IOException {
        doTest("bsp_5x5");
    }

    @Test
    public void test_Beispiel_6x6() throws IOException {
        doTest("bsp_6x6");
    }

    @Test
    public void test_Beispiel_8x8() throws IOException {
        doTest("bsp_8x8");
    }

    @Test
    public void test_Beispiel_14x14() throws IOException {
        doTest("bsp_14x14");
    }

    @Test
    public void test_Beispiel_25x25() throws IOException {
        doTest("bsp_25x25");
    }

    @Test
    public void test_Isolation_1() throws IOException {
        doTest("test_isolation_1");
    }

    @Test
    public void test_Isolation_2() throws IOException {
        doTest("test_isolation_2");
    }

    @Test
    public void test_Isolation_3() throws IOException {
        doTest("test_isolation_3");
    }

    @Test
    public void test_Isolation_4() throws IOException {
        doTest("test_isolation_4");
    }

    @Test
    public void test_Isolation_5() throws IOException {
        doTest("test_isolation_5");
    }

    // TODO weitere Tests werden folgen ... ;-)
}