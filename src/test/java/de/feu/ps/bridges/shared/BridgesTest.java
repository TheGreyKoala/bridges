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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Diese Klasse stellt eine abstrakte Klasse dar, mit deren Hilfe die
 * Korrektheit des Algorithmus zum Lösen eines Bridges-Rätsels überprüft werden
 * kann.
 *
 * @author ProPra - FernUniversität Hagen
 */
public abstract class BridgesTest {

    /**
     * Beim Testen werden der Lösungsalgorithmus und der Algortihmus zum
     * Generieren neuer Rätsel überprüft. Für die Unterscheidung der Test-Art
     * gibt es diesen Enum.
     *
     * @author ProPra - FernUniversität Hagen
     */
    private enum Typ {
        /** Test-Art Lösungsalgorithmus */
        SOLVE,

        /** Test-Art Algorithmus zum Generieren neuer Rätsel */
        GENERATE
    }

    /** Verzeichnis für die Eingabedateien */
    private final static String DATA_DIR = getDataDir();

    private static String getDataDir() {
        try {
            return BridgesTest.class.getResource("data").toURI().getPath();
        } catch (URISyntaxException e) {
            return "";
        }
    }

    /** Unterverzeichnis für die Ausgabedateien */
    private final static String TEMP_DIR = "temp";

    /** Dateierweiterung für eine Rätseldatei (Eingabedatei) */
    private final static String EXT_PUZZLE = "bgs";

    /** Dateikennung für die Vergleichsdatei (vorgegebene Lösung) */
    private final static String EXT_SOLUTION = "sol";

    /** Dateikennung für die berechnete Lösung des Rätsels */
    private final static String EXT_CALCULATED = "calc";

    /** Dateikennung für neu generierte Rätsel */
    private final static String EXT_GENERATED = "gen";

    /** String für die Angabe Name, Matrikel-Nr. und E-Mailadresse des Studenten */
    private static String strNameMatrikelnummer = null;

    /** Startzeit für den gesamten Testlauf */
    private static long startTime;

    /** für die Zusammenfassung der Testergebnisse (solve) */
    private static StringBuffer sbSummarySolve = new StringBuffer();

    /** für die Zusammenfassung der Testergebnisse (generate) */
    private static StringBuffer sbSummaryGenerate = new StringBuffer();

    /** Zähler für alle Testfälle */
    private static int counter = 0;

    /** Zähler für fehlgeschlagene Testfälle */
    private static int counterFailed = 0;

    /** Test-Art */
    private Typ testTyp;

    /** für den Test verwendeter Dateiname */
    private String filename;

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
     * Dies ist eine Hilfsmethode.
     *
     * <p>
     * Diese Methode erzeugt einen regulären Ausdruck, mit dem der Aufbau einer
     * bgs-Datei überprüft werden kann.
     * </p>
     *
     * @param width
     *            Breite eines Rätsels
     * @param height
     *            Höhe eines Rätsels
     * @param isles
     *            Anzahl der Insel
     * @return ein String, der einen regulären Ausdruck für ein Rätsel enthält
     */
    private String createRegExp(int width, int height, int isles) {
        StringBuffer sb = new StringBuffer();

        sb.append("^FIELD");
        sb.append(width);
        sb.append("x");
        sb.append(height);
        sb.append("\\|");
        sb.append(isles);
        sb.append("ISLANDS(\\((0");
        for (int b = 1; b < width; b++) {
            sb.append("|");
            sb.append(b);
        }
        sb.append("),(0");
        for (int h = 1; h < height; h++) {
            sb.append("|");
            sb.append(h);
        }
        sb.append(")\\|[1-8]\\)){");
        sb.append(isles);
        sb.append("}(BRIDGES|)$");

        return sb.toString();
    }

    /**
     * Dies ist die Test-Methode, die zu einem Rätsel die berechnete Lösung mit
     * der erwarteten Lösung vergleicht.
     *
     * @param puzzle
     *            Name des Rätsels (entspricht dem Dateinamen ohne
     *            Dateierweiterung
     * @throws IOException
     *             wenn bei der Dateiverarbeitung eine IOException auftritt
     */
    public void doTestSolve(String puzzle) throws IOException {
        testTyp = Typ.SOLVE;

        // Der Eingabedateiname wird für das Testergebnis benötigt.
        filename = puzzle + "." + EXT_PUZZLE;

        // Zusammenbauen der Dateinamen (Eingabedatei, Vergleichsdatei,
        // Ausgabedatei)
        String filenamePuzzle = DATA_DIR + File.separator + puzzle + "."
                + EXT_PUZZLE;
        String filenameExpected = DATA_DIR + File.separator + puzzle + "."
                + EXT_SOLUTION + "." + EXT_PUZZLE;
        String filenameCalculated = DATA_DIR + File.separator + TEMP_DIR
                + File.separator + getMatrNr() + "_" + puzzle + "."
                + EXT_CALCULATED + "." + EXT_PUZZLE;

        // Eventuell existierende Datei (filenameCalculated) löschen.
        if (Files.exists(Paths.get(filenameCalculated))) {
            Files.delete(Paths.get(filenameCalculated));
        }

        // Aufruf der Testmethode
        BridgesTester tester = getBridgesTesterImpl();
        tester.testSolvePuzzle(filenamePuzzle, filenameCalculated);

        // Umwandeln der berechneten Lösung in einen String für den späteren
        // Vergleich
        String calculatedSolution = convertFileToString(filenameCalculated);

        if (Files.exists(Paths.get(filenameExpected))) {
            // Die Datei puzzle.sol.bgs existiert, dann gibt es nur diese eine
            // Lösung.

            // Umwandeln der erwarteten Lösung für den Vergleich
            String expectedSolution = convertFileToString(filenameExpected);

            // Vergleich
            assertEquals("Die berechnete Lösung ist nicht korrekt.",
                    expectedSolution, calculatedSolution);
        } else {
            // Zum Testen, ob mindestens eine Datei mit einer erwarteten
            // Lösung existiert.
            boolean expectedSolutionExists = false;

            // Es gibt mehrere Lösungen puzzle.sol_*.bgs, von denen eine
            // mit der berechneten Lösung übereinstimmen muss.
            boolean bool = false;

            String filter = "^" + puzzle + "." + EXT_SOLUTION + "_" + "(\\d+)"
                    + "." + EXT_PUZZLE + "$";
            for (String filenameSolX : new File(DATA_DIR).list()) {

                if (Pattern.matches(filter, filenameSolX)) {
                    expectedSolutionExists = true;

                    // Umwandeln der möglichen Lösung für den Vergleich
                    String expectedSolution = convertFileToString(DATA_DIR
                            + File.separator + filenameSolX);

                    // Vergleich
                    if (expectedSolution.equals(calculatedSolution)) {
                        bool = true;
                        break;
                    }
                }
            }

            assertTrue("Keine Dateien mit erwarteter Lösung vorhanden (\""
                            + puzzle + "." + EXT_SOLUTION + "*." + EXT_PUZZLE + "\"). "
                            + "Führen Sie bitte noch ein SVN-Update auf dem "
                            + "Projekt \"de.feu.ps.bridges.puzzles\" aus.",
                    expectedSolutionExists);
            assertTrue("Keine der möglichen Lösungen \"" + puzzle + "."
                            + EXT_SOLUTION + "_*." + EXT_PUZZLE + "\" wurde erreicht.",
                    bool);
        }
    }

    /**
     * Dies ist die Test-Methode, die den Algortihmus zum Generieren eines neuen
     * Rätsels überprüft.
     * <p>
     * Neben der Prüfung auf Plausibilität der Parameter <tt>width</tt>,
     * <tt>height</tt> und <tt>isles</tt> wird bei dem Test lediglich überprüft,
     * ob die Ausgabe des generierten Rätsels der korrekte Syntax entspricht.
     * </p>
     *
     * @param width
     *            Breite des Rätsels
     * @param height
     *            Höhe des Rätsels
     * @param isles
     *            Anzahl der Inseln
     * @throws IOException
     *             wenn bei der Dateiverarbeitung eine IOException auftritt
     */
    public void doTestGenerate(int width, int height, int isles)
            throws IOException {
        testTyp = Typ.GENERATE;

        String puzzle = getMatrNr() + "_" + width + "x" + height + "_" + isles;

        // Der Eingabedateiname wird für das Testergebnis benötigt.
        filename = puzzle + "." + EXT_GENERATED + "." + EXT_PUZZLE;

        // Zusammenbauen des Dateinamens (Ausgabedatei)
        String filenameGenerated = DATA_DIR + File.separator + TEMP_DIR
                + File.separator + filename;

        // Eventuell existierende Datei (filenameGenerated) löschen.
        if (Files.exists(Paths.get(filenameGenerated))) {
            Files.delete(Paths.get(filenameGenerated));
        }

        // Aufruf der Testmethode
        BridgesTester tester = getBridgesTesterImpl();
        try {
            tester.testGeneratePuzzle(filenameGenerated, width, height, isles);
        } catch (IllegalArgumentException e) {
            filename = "---";
            throw new IllegalArgumentException(e);
        }

        // Die generierte Datei wird in einen String umgewandelt.
        String generatedPuzzle = convertFileToString(filenameGenerated);

        // Die Überprüfung wird mit Hilfe eines regulären Ausdrucks
        // durchgeführt.
        String regExp = createRegExp(width, height, isles);

        // Jetzt wird geprüft, ob das generierte Rätsel dem regulären Ausdruck
        // entspricht.
        assertTrue("Das Rätsel enthält syntaktische/semantische Fehler.",
                Pattern.matches(regExp, generatedPuzzle));
    }

    /**
     * Vor dem Ausführen aller Tests wird das temporäre Verzeichnis angelegt und
     * die Startzeit gespeichert.
     *
     * @throws IOException
     */
    @BeforeClass
    public static void setStartTime() throws IOException {
        Path tempPath = Paths.get(DATA_DIR + File.separator + TEMP_DIR);
        if (!Files.exists(tempPath)) {
            Files.createDirectory(tempPath);
        }

        startTime = System.currentTimeMillis();
    }

    /**
     * Wenn alle Tests beendet sind, soll eine Zusammenfassung auf der Konsole
     * ausgegeben werden.
     */
    @AfterClass
    public static void printSummary() {
        double seconds = (System.currentTimeMillis() - startTime) / 1000.0;

        StringBuffer sb = new StringBuffer();

        // Ausgabe einer Zusammenfassung des Testergebnisses auf der Konsole

        sb.append("================================================================================================\n");
        sb.append("Bridges Test - Zusammenfassung für \n");
        sb.append(strNameMatrikelnummer + "\n");

        sb.append("================================================================================================\n");
        sb.append(String.format(
                "| %-30.30s | %-30.30s | %-9.9s | %14.14s | %n",
                "Methode (testSolvePuzzle)", "Eingabedatei", "Erfolg",
                "benötigte Zeit"));
        sb.append("================================================================================================\n");

        sb.append(sbSummarySolve);

        sb.append("================================================================================================\n");
        sb.append(String.format(
                "| %-30.30s | %-30.30s | %-9.9s | %14.14s | %n",
                "Methode (testGeneratePuzzle)", "generierte Datei", "Erfolg",
                "benötigte Zeit"));
        sb.append("================================================================================================\n");

        sb.append(sbSummaryGenerate);

        sb.append("================================================================================================\n");

        sb.append(counter + " Test(s) bearbeitet, " + (counter - counterFailed)
                + " Test(s) bestanden\n");
        sb.append(String.format("Benötigte Zeit: %.3f s\n", seconds));

        if (counterFailed > 0) {
            sb.append("\nHinweise:\n"
                    + "- Bei fehlerhaften Testfällen können Sie "
                    + "die entsprechenden Dateien \"*." + EXT_SOLUTION + "*."
                    + EXT_PUZZLE + "\" " + "und \n  \"" + TEMP_DIR
                    + File.separator + "*." + EXT_CALCULATED + "." + EXT_PUZZLE
                    + "\"" + " im Verzeichnis \"" + DATA_DIR + "\""
                    + " vergleichen.");
        }

        System.out.println(sb);
    }

    /**
     * TestWatcher wird für das eigene Reporting verwendet.
     */
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
                    "| %-30.30s | %-30.30s | %-9.9s | %12.3f s | %s%n",
                    description.getMethodName(), filename, result, seconds,
                    message);

            switch (testTyp) {
                case SOLVE:
                    sbSummarySolve.append(line);
                    break;
                case GENERATE:
                    sbSummaryGenerate.append(line);
                    break;
            }

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

    // ---- Tests für den Lösungsalgorithmus ----

    @Test
    public void test_Beispiel_Abb2() throws IOException {
        doTestSolve("bsp_abb2");
    }

    @Test
    public void test_Beispiel_Abb22() throws IOException {
        doTestSolve("bsp_abb22");
    }

    @Test
    public void test_Beispiel_5x5() throws IOException {
        doTestSolve("bsp_5x5");
    }

    @Test
    public void test_Beispiel_6x6() throws IOException {
        doTestSolve("bsp_6x6");
    }

    @Test
    public void test_Beispiel_8x8() throws IOException {
        doTestSolve("bsp_8x8");
    }

    @Test
    public void test_Beispiel_14x14() throws IOException {
        doTestSolve("bsp_14x14");
    }

    @Test
    public void test_Beispiel_25x25() throws IOException {
        doTestSolve("bsp_25x25");
    }

    @Test
    public void test_Isolation_1() throws IOException {
        doTestSolve("test_isolation_1");
    }

    @Test
    public void test_Isolation_2() throws IOException {
        doTestSolve("test_isolation_2");
    }

    @Test
    public void test_Isolation_3() throws IOException {
        doTestSolve("test_isolation_3");
    }

    /**
     * Bei diesem Testfall wird ein Lösungsalgorithmus an einen Punkt gelangen,
     * an dem das Hinzfügen weiterer sicherer Brücken etwas kompliziert ist.
     * Daher ist es ausreichend, wenn ein Lösungsalgorithmus an diesem Punkt
     * nicht weiter kommt. Für die Überprüfung wird daher mit mehreren möglichen
     * Lösungen verglichen.
     *
     * @throws IOException
     *             wenn bei der Dateiverarbeitung eine IOException auftritt
     */
    @Test
    public void test_Isolation_4() throws IOException {
        doTestSolve("test_isolation_4");
    }

    /**
     * Bei diesem Testfall wird ein Lösungsalgorithmus an einen Punkt gelangen,
     * an dem das Hinzfügen weiterer sicherer Brücken etwas kompliziert ist.
     * Daher ist es ausreichend, wenn ein Lösungsalgorithmus an diesem Punkt
     * nicht weiter kommt. Für die Überprüfung wird daher mit mehreren möglichen
     * Lösungen verglichen.
     *
     * @throws IOException
     *             wenn bei der Dateiverarbeitung eine IOException auftritt
     */
    @Test
    public void test_Isolation_5() throws IOException {
        doTestSolve("test_isolation_5");
    }

    // ---- Tests für den Algorithmus zum Generieren neuer Rätsel ----

    // ---- fehlerhafte Parameter für "width", "height" oder "isles"

    /** Breite zu klein: 3 x 4 mit 2 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_3x4with2() throws IOException {
        doTestGenerate(3, 4, 2);
    }

    /** Hoehe zu klein: 4 x 3 mit 2 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_4x3with2() throws IOException {
        doTestGenerate(4, 3, 2);
    }

    /** Inselanzahl nicht erlaubt: 4 x 4 mit 1 Insel */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_4x4with1() throws IOException {
        doTestGenerate(4, 4, 1);
    }

    /** Inselanzahl zu groß: 4 x 4 mit 4 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_4x4with4() throws IOException {
        doTestGenerate(4, 4, 4);
    }

    /** Hoehe zu gross: 4 x 26 mit 2 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_4x26with2() throws IOException {
        doTestGenerate(4, 26, 2);
    }

    /** Inselanzahl zu groß: 5 x 6 mit 7 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_5x6with7() throws IOException {
        doTestGenerate(5, 6, 7);
    }

    /** Inselanzahl nicht erlaubt: 25 x 25 mit 126 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_25x25with126() throws IOException {
        doTestGenerate(25, 25, 126);
    }

    /** Breite zu groß: 26 x 4 mit 2 Inseln */
    @Test(expected = IllegalArgumentException.class)
    public void test_generate_26x4with2() throws IOException {
        doTestGenerate(26, 4, 2);
    }

    // ---- plausible Parameter für "width", "height" oder "isles"

    /** minimal mögliche Größe: 4 x 4 mit 2 Inseln */
    @Test
    public void test_generate_4x4with2() throws IOException {
        doTestGenerate(4, 4, 2);
    }

    /** minimal mögliche Größe: 4 x 4 mit 3 Inseln */
    @Test
    public void test_generate_4x4with3() throws IOException {
        doTestGenerate(4, 4, 3);
    }

    /** erlaubte Größe: 4 x 25 mit 20 Inseln */
    @Test
    public void test_generate_4x25with20() throws IOException {
        doTestGenerate(4, 25, 20);
    }

    /** erlaubte Größe: 5 x 6 mit 6 Inseln */
    @Test
    public void test_generate_5x6with6() throws IOException {
        doTestGenerate(5, 6, 6);
    }

    /** erlaubte Größe: 11 x 18 mit 24 Inseln */
    @Test
    public void test_generate_11x18with24() throws IOException {
        doTestGenerate(11, 18, 24);
    }

    /** erlaubte Größe: 25 x 10 mit 36 Inseln */
    @Test
    public void test_generate_25x10with36() throws IOException {
        doTestGenerate(25, 10, 36);
    }

    /** maximal mögliche Größe: 25 x 25 mit 125 Inseln */
    @Test
    public void test_generate_25x25with125() throws IOException {
        doTestGenerate(25, 25, 125);
    }

    // TODO weitere Tests werden folgen ... ;-)
}