package de.feu.ps.bridges.gui.controller;

import de.feu.ps.bridges.analyser.PuzzleStatus;
import de.feu.ps.bridges.gui.gamestate.GameState;
import de.feu.ps.bridges.gui.gamestate.GameStateEvent;
import de.feu.ps.bridges.gui.gamestate.GameStateEventType;
import de.feu.ps.bridges.gui.gamestate.GameStateListener;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

import static de.feu.ps.bridges.gui.gamestate.GameStateEventType.*;

/**
 * @author Tim Gremplewski
 */
class AlertHelper implements GameStateListener {

    private final ResourceBundle resourceBundle;
    private final GameState gameState;

    AlertHelper(final ResourceBundle resourceBundle, final GameState gameState) {
        this.resourceBundle = resourceBundle;
        this.gameState = gameState;
    }

    // TODO: Split into: PuzzleStatus, Error, Other events

    @Override
    public void handleGameStateEvent(final GameStateEvent event) {
        final GameStateEventType eventType = event.getGameStateEventType();
        final PuzzleStatus puzzleStatus = gameState.getPuzzleStatus();

        if (eventType == NO_NEXT_MOVE) {
            if (puzzleStatus == PuzzleStatus.UNSOLVED) {
                getStatusInformationAlert(PuzzleStatus.UNSOLVED, resourceBundle.getString("noNextMoveDialog.contentText"))
                        .showAndWait();
            }
        }

        if (eventType == PUZZLE_CHANGED || eventType == AUTOMATIC_SOLVING_CANCELLED_BY_USER
                || eventType == NO_NEXT_MOVE) {

            if (puzzleStatus != PuzzleStatus.UNSOLVED) {
                getStatusInformationAlert(puzzleStatus).showAndWait();
            }
        }

        if (eventType.isErrorState() || eventType == INVALID_MOVE) {
            getEventAlert(eventType).showAndWait();
        }

        if (eventType == AUTOMATIC_SOLVING_FINISHED) {
            if (puzzleStatus == PuzzleStatus.UNSOLVED) {
                getStatusInformationAlert(puzzleStatus).showAndWait();
            }
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

    private Alert getEventAlert(final GameStateEventType gameStateEventType) {
        if (gameStateEventType.isErrorState()) {
            return getErrorAlert(gameStateEventType);
        } else {
            final Alert alert = new Alert(Alert.AlertType.WARNING);
            switch (gameStateEventType) {
                case INVALID_MOVE:
                    alert.setTitle(resourceBundle.getString("warning.title"));
                    alert.setHeaderText(resourceBundle.getString("invalidMoveDialog.headerText"));
                    alert.setContentText(resourceBundle.getString("invalidMoveDialog.contentText"));
                    break;
            }
            return alert;
        }
    }

    private Alert getErrorAlert(final GameStateEventType gameStateEventType) {
        switch (gameStateEventType) {
            case PUZZLE_GENERATION_FAILED:
                return getErrorAlert(resourceBundle.getString("generation.failed"));
            case LOADING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("loading.failed"));
            case SAVING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("saving.failed"));
            case RESTARTING_PUZZLE_FAILED:
                return getErrorAlert(resourceBundle.getString("restarting.failed"));
            case NEXT_MOVE_FAILED:
                return getErrorAlert(resourceBundle.getString("nextMove.failed"));
            case SOLVING_FAILED:
                return getErrorAlert(resourceBundle.getString("solving.failed"));
            case BUILD_BRIDGE_FAILED:
                return getErrorAlert(resourceBundle.getString("buildBridge.failed"));
            case TEAR_DOWN_BRIDGE_FAILED:
                return getErrorAlert(resourceBundle.getString("tearDownBridge.failed"));
        }
        return null;
    }

    private Alert getErrorAlert(final String headerText) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error.title"));
        alert.setHeaderText(headerText);
        alert.setContentText(resourceBundle.getString("error.content.text"));
        return alert;
    }
}
