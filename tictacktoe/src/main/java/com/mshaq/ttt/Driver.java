package com.mshaq.ttt;

public class Driver {

    public static void main(String[] args) {
        Game game = new Game();
        String status = game.start();
        System.out.println(status.equals("tie") ? "Match Tied" : status + " Won!");
    }
}
