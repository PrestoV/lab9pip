package models.db;

import models.area.Point;
import models.area.PointRepository;
import models.area.UserPoint;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;


public class JPAPointRepository implements PointRepository {
    private final JPAApi jpaApi;

    @Inject
    public JPAPointRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public void add(UserPoint point) {
        wrap(em -> insert(em, point));
    }

    @Override
    public List<Point> list(String owner) {
        return wrap(em -> getPoints(em, owner));
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Point insert(EntityManager em, Point point) {
        em.persist(point);
        return point;
    }

    private List getPoints(EntityManager em, String owner) {
        return em.createQuery("SELECT p FROM UserPoint p WHERE p.owner=:owner")
                .setParameter("owner", owner)
                .getResultList();
    }
}
