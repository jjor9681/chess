package model;

// ChessGames are used in this record, so we need to include ChessGame.
import chess.ChessGame;

public record GameData(
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName,
        ChessGame game
) {}
