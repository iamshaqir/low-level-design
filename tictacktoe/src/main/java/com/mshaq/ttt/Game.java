package com.mshaq.ttt;

import com.mshaq.ttt.model.*;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class Game {

    public record Player(String name, PlayingPiece playingPiece) {
    }

    private final Deque<Player> players;
    private final Board board;

    public Game() {
        players = new LinkedList<>();
        board = new Board(3);
        initGame();
    }

    private void initGame() {
        Player one = new Player("Player-1", new PieceX());
        Player two = new Player("Player-2", new PieceO());

        players.add(one);
        players.add(two);
    }

    public String start() {
        boolean noWinner = true;
        board.printBoard();
        while (noWinner) {
            Player currentPlayer = players.removeFirst();
            if (!board.isEmpty()) {
                noWinner = false;
                continue;
            }
            System.out.printf("%s, Enter row,column: ", currentPlayer.name());

            Scanner scanner = new Scanner(System.in);
            String playerResponse = scanner.nextLine();
            String[] values = playerResponse.split(",");

            Integer row = Integer.valueOf(values[0]);
            Integer column = Integer.valueOf(values[1]);

            boolean isAdded = board.addPiece(row, column, currentPlayer.playingPiece());
            if (!isAdded) {
                System.out.println("Incorrect position, please try again!");
                players.addFirst(currentPlayer);
                continue;
            }
            board.printBoard();
            players.addLast(currentPlayer);
            PieceType pieceType = currentPlayer.playingPiece().getPieceType();
            if (isWinner(pieceType, row, column)) {
                return currentPlayer.name();
            }
        }
        return "tie";
    }

    private boolean isWinner(PieceType pieceType, Integer row, Integer column) {
        boolean rowMatch = true, columnMatch = true, diagonalMatch = true, revDiagonal = true;
        PlayingPiece[][] currentBoardState = board.getBoard();

        // check for row match
        for (int i = 0; i < currentBoardState.length; i++) {
            if (currentBoardState[row][i] == null || currentBoardState[row][i].getPieceType() != pieceType) {
                rowMatch = false;
                break;
            }
        }
        // check for column match
        for (int i = 0; i < currentBoardState[0].length; i++) {
            if (currentBoardState[i][column] == null || currentBoardState[i][column].getPieceType() != pieceType) {
                columnMatch = false;
                break;
            }
        }
        // check for diagonal match
        for (int i = 0, j = 0; i < currentBoardState.length; i++, j++) {
            if (currentBoardState[i][j] == null || currentBoardState[i][j].getPieceType() != pieceType) {
                diagonalMatch = false;
                break;
            }
        }
        //check for reverse diagonal match
        for (int i = 0, j = currentBoardState.length - 1; i < currentBoardState.length && j >= 0; i++, j--) {
            if (currentBoardState[i][j] == null || currentBoardState[i][j].getPieceType() != pieceType) {
                revDiagonal = false;
                break;
            }
        }
        return rowMatch || columnMatch || diagonalMatch || revDiagonal;
    }
}
