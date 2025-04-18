package com.mshaq.ttt;

import com.mshaq.ttt.model.PlayingPiece;

public class Board {
    private final PlayingPiece[][] board;

    public Board(final int size) {
        board = new PlayingPiece[size][size];
    }

    public PlayingPiece[][] getBoard() {
        return board;
    }

    public void printBoard() {
        for (PlayingPiece[] playingPieces : board) {
            for (PlayingPiece playingPiece : playingPieces) {
                if (playingPiece != null) {
                    System.out.print(playingPiece.getPieceType().name() + "   ");
                } else {
                    System.out.print("    ");
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
    }

    public boolean isEmpty() {
        for (PlayingPiece[] playingPieces : board) {
            for (PlayingPiece playingPiece : playingPieces) {
                if (playingPiece == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addPiece(Integer row, Integer column, PlayingPiece playingPiece) {
        if (board[row][column] != null) return false;
        board[row][column] = playingPiece;
        return true;
    }
}