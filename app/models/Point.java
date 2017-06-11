package models;

import java.io.Serializable;


public class Point implements Serializable{
    private Double x;
    private Double y;
    private Double r;
    private Boolean inArea;

    public Point(Double x, Double y, Double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    private Point(Double x, Double y, Double r, Boolean inArea) {
        this(x, y, r);
        this.inArea = inArea;
    }

    void setInArea(Boolean inArea) {
        this.inArea = inArea;
    }

    public Boolean getInArea() {
        return inArea;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }

    @Override
    public Point clone() {
        return new Point(x, y, r, inArea);
    }
}