package client.gameplay;

import chess.ChessGame;
import chess.ChessPosition;
import ui.GameBuilder;

public class Board {

    private final GameBuilder gameBuilder =
            new GameBuilder();

    private final String perspective;

    private ChessGame currentGame;

    public Board(String perspective) {
        this.perspective = perspective;
    }

    public void updateGame(ChessGame game) {
        currentGame = game;
    }

    public String redraw() {
        if (currentGame == null) {
            return "No game loaded yet.\n";
        }

        if (perspective.equals("BLACK")) {
            return gameBuilder.buildBlackBoard(currentGame);
        }

        return gameBuilder.buildWhiteBoard(currentGame);
    }

    public String highlight(ChessPosition position) {
        if (currentGame == null) {
            return "No game loaded yet.\n";
        }

        boolean whitePerspective =
                !perspective.equals("BLACK");

        return gameBuilder.buildHighlightedBoard(
                currentGame,
                position,
                whitePerspective);
    }
}