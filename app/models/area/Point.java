package models.area;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="points")
public class Point implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Double x;
    private Double y;
    private Double r;
    private Boolean inarea;

    public Point(Double x, Double y, Double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Point() {
    }

    private Point(Double x, Double y, Double r, Boolean inarea) {
        this(x, y, r);
        this.inarea = inarea;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Boolean getInarea() {
        return inarea;
    }

    public void setInarea(Boolean inArea) {
        this.inarea = inArea;
    }

    @Override
    public Point clone() {
        return new Point(x, y, r, inarea);
    }
}