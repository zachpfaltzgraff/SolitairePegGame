package com.example.solitairepeggame;

import javafx.scene.control.Button;

public class Peg extends Button {
    private int i;
    private int k;
    private boolean occupied;
    public Peg() {
        super();
    }

    public Peg(int i, int k) {
        this.i = i;
        this.k = k;
        this.occupied = true;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
