package com.example.feetflee.game_logic;

public class GridItem {
    private int row;
    private int column;
    private DIRECTION dir;

    public GridItem() {
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public DIRECTION getDir() {
        return dir;
    }

    public GridItem setRow(int row) {
        this.row = row;
        return this;
    }

    public GridItem setColumn(int column) {
        this.column = column;
        return this;
    }

    public void setDir(int next){
        try {
            dir = DIRECTION.values()[next];
        }catch (Exception ex){

        }
    }

    public void decreaseRow() { row--; }
    public void increaseRow() {
        row++;
    }
    public void decreaseColumn() {
        column--;
    }
    public void increaseColumn() {
        column++;
    }

}
