package com.mshaq.ttt.model;

public abstract class PlayingPiece {

    private final PieceType pieceType;

    protected PlayingPiece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}
