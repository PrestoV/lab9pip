package models.area;

import com.google.inject.ImplementedBy;
import models.db.JPAPointRepository;

import java.util.List;


@ImplementedBy(JPAPointRepository.class)
public interface PointRepository {
    void add(UserPoint point);
    List<Point> list(String owner);
}
