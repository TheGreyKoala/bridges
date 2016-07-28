package de.feu.ps.bridges.gui.listeners;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.events.AutomatedSolvingEvent;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.events.GameStateEvent;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.CANCELLED_BY_USER;
import static de.feu.ps.bridges.gui.events.AutomatedSolvingEvent.FINISHED;
import static de.feu.ps.bridges.gui.events.GameStateEvent.*;

/**
 * @author Tim Gremplewski
 */
public class PuzzleStatusAlert implements GameStateEventListener, AutomatedSolvingEventListener {

    private final ResourceBundle resourceBundle;
    private final GameState gameState;

    public PuzzleStatusAlert(final ResourceBundle resourceBundle, final GameState gameState) {
        this.resourceBundle = resourceBundle;
        this.gameState = gameState;
    }

    @Override
    public void handleEvent(final GameStateEvent event) {
        final PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();

        if (event == NO_NEXT_MOVE && puzzleStatus == PuzzleStatus.UNSOLVED) {
            getStatusInformationAlert(PuzzleStatus.UNSOLVED, resourceBundle.getString("noNextMoveDialog.contentText"))
                .showAndWait();
        } else if (event == PUZZLE_CHANGED && puzzleStatus != PuzzleStatus.UNSOLVED) {
            getStatusInformationAlert(puzzleStatus)
                .showAndWait();
        }
    }

    @Override
    public void handleEvent(final GameStateEvent event, final Object eventParameter) {
    }

    @Override
    public void handleEvent(final AutomatedSolvingEvent event) {
        final PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();

        if ((event == CANCELLED_BY_USER && puzzleStatus != PuzzleStatus.UNSOLVED)
            || (event == FINISHED && puzzleStatus == PuzzleStatus.UNSOLVED)) {

            getStatusInformationAlert(puzzleStatus)
                .showAndWait();
        }
    }

    private Alert getStatusInformationAlert(final PuzzleStatus puzzleStatus, final String customContentText) {
        final Alert statusInformationAlert = getStatusInformationAlert(puzzleStatus);
        statusInformationAlert.setContentText(customContentText);
        return statusInformationAlert;
    }

    private Alert getStatusInformationAlert(final PuzzleStatus puzzleStatus) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (puzzleStatus) {
            case SOLVED:
                alert.setTitle(resourceBundle.getString("autoSolveDialog.solved.title"));
                alert.setHeaderText(resourceBundle.getString("autoSolveDialog.solved.title"));
                alert.setContentText(resourceBundle.getString("autoSolveDialog.solved.contentText"));
                break;
            case UNSOLVED:
                alert.setTitle(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setHeaderText(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setContentText(resourceBundle.getString("autoSolveDialog.unsolved.contentText"));
                break;
            case UNSOLVABLE:
                alert.setTitle(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setHeaderText(resourceBundle.getString("autoSolveDialog.unsolved.title"));
                alert.setContentText(resourceBundle.getString("autoSolveDialog.unsolvable.contentText"));
                break;
        }
        return alert;
    }
}
