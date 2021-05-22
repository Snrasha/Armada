package src.data.scripts.utils;

public class PulseIndustry_Cell {

    private int x;
    private int y;
    private int number;

    public PulseIndustry_Cell(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int decrementation() {
        return (--number);
    }
}
