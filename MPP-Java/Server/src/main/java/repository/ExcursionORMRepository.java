package repository;

import domain.Excursion;
import domain.validators.ValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ExcursionORMRepository implements ExcursionRepository{

    private static SessionFactory sessionFactory;

    public static void setSessionFactory(SessionFactory sessionFactory) {
        ExcursionORMRepository.sessionFactory = sessionFactory;
    }


    @Override
    public List<Excursion> filterByObjective(String objective, LocalTime from, LocalTime to) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Excursion> excursionList = session.createQuery("from Excursion where start_time between :from and :to and objective like :objective", Excursion.class)
                        .setParameter("from", from.toString())
                        .setParameter("to", to.toString())
                        .setParameter("objective", objective)
                        .list();
                tx.commit();
                return excursionList;
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Excursion findOne(Long aLong) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Excursion excursion = session.createQuery("from Excursion where id = :id", Excursion.class).setParameter("id", aLong).getSingleResult();                tx.commit();
                return excursion;
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return null;
    }

    @Override
    public List<Excursion> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Excursion> excursionList = session.createQuery("from Excursion", Excursion.class).list();
                tx.commit();
                return excursionList;
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void save(Excursion entity) throws ValidationException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
    }

    @Override
    public void delete(Long aLong) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Excursion excursion = session.createQuery("from Excursion where id = :id", Excursion.class).setParameter("id", aLong).getSingleResult();
                session.delete(excursion);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
    }

    @Override
    public void update(Long aLong, Excursion entity) throws ValidationException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Excursion excursion = session.load(Excursion.class, aLong);
                excursion.setSeats(entity.getSeats());
                excursion.setCompany(entity.getCompany());
                excursion.setObjective(entity.getObjective());
                excursion.setPrice(entity.getPrice());
                excursion.setTime(entity.getTime());
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
    }
}
