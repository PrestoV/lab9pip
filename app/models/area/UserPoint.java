package models.area;

import javax.persistence.*;

@Entity
public class UserPoint extends Point {
    private String owner;

    public UserPoint() {
    }

    public UserPoint(String owner, Double x, Double y, Double r) {
        super(x, y, r);
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
