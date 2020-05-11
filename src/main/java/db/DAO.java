package db;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * @author Michael König
 */
public class DAO implements AutoCloseable {

    private final static DAO INSTANCE = new DAO();

    // private Constructor
    private DAO() {
    }

    public static DAO getINSTANCE() {
        return INSTANCE;
    }

    public boolean insertTestData() {
        Customer koenig = new Customer(111111, "Michael", "König");
        Customer hoelzl = new Customer(111112, "Manuel", "Hölzl");
        Customer zach = new Customer(111113, "Alexander", "Zach");

        Station stp = new Station("St. Pölten");
        Station krems = new Station("Krems");
        Station wien = new Station("Wien");

        Car c1 = new Car("1", 2001, 1000, "Model 1", stp, null);
        Car c2 = new Car("2", 2002, 2000, "Model 2", krems, null);
        Car c3 = new Car("3", 2003, 3000, "Model 3", null, null);
        Car c4 = new Car("4", 2004, 4000, "Model 4", null, null);
        Car c5 = new Car("5", 2005, 5000, "Model 5", null, null);
        Car c6 = new Car("6", 2006, 6000, "Model 6", null, null);
        Car c7 = new Car("7", 2007, 7000, "Model 7", null, null);
        Car c8 = new Car("8", 2008, 8000, "Model 8", null, null);
        Car c9 = new Car("9", 2009, 9000, "Model 9", null, null);
        Car c10 = new Car("10", 2010, 10000, "Model 10", null, null);

        Rental r1 = new Rental(80, LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 2), krems, wien, c3, koenig);
        Rental r2 = new Rental(25, LocalDate.of(2020, 5, 2), LocalDate.of(2020, 5, 3), stp, krems, c4, hoelzl);
        Rental r3 = new Rental(90, LocalDate.of(2020, 5, 3), LocalDate.of(2020, 5, 3), wien, stp, c5, zach);
        Rental r4 = new Rental(null, LocalDate.of(2020, 05, 04), null, krems, null, c6, koenig);
        Rental r5 = new Rental(null, LocalDate.of(2020, 05, 05), null, stp, null, c6, hoelzl);


        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            // customers, stations and cards 3-6 are being persisted implicitly by cascades
            tx.begin();

            em.persist(c1);
            em.persist(c2);
            em.persist(c7);
            em.persist(c8);
            em.persist(c9);
            em.persist(c10);
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(r4);
            em.persist(r5);

            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Speichert eine neue Station in der Datenbank
    // liefert true bei Erfolg, false bei einem Fehler
    public boolean insertStation(Station station) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(station);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Speichert ein neues Fahrzeug in der Datenbank
    // liefert true bei Erfolg, false bei einem Fehler
    public boolean insertCar(Car car) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(car);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Speichert einen neuen Mieter in der Datenbank
    // liefert true bei Erfolg, false bei einem Fehler
    public boolean insertCustomer(Customer c) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(c);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Speichert einen neuen Mietvorgang in der Datenbank
    // liefert true bei Erfolg, false bei einem Fehler
    public boolean insertRental(Rental rental) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(rental);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Liefert eine Liste aller Fahrzeuge, die in der Station st
    // verfügbar sind.
    public List<Car> findCarsByStation(Station station) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Car> q = em.createQuery("select c from Car c where c.location = :station", Car.class);
        q.setParameter("station", station);
        List<Car> result = q.getResultList();
        em.close();
        return result;
    }

    // Liefert eine Liste aller Mietvorgänge, die der Customer c
    // getätigt hat.
    public List<Rental> findRentalsByCustomer(Customer c) {
        if (c == null) throw new IllegalArgumentException("Customer must not be null");
        Integer cn = c.getCustomerNumber();
        if (cn == null) throw new IllegalArgumentException("customerNumber must not be null");

        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Rental> q = em.createQuery("select r from Rental r where r.driver = :cn", Rental.class);
        q.setParameter("cn", cn);
        List<Rental> result = q.getResultList();
        em.close();
        return result;
    }

    // Schließt den Mietvorgang r mit dem Rueckgabedatum returnDate
    // bei der Station returnStation ab, wobei km die gefahrenen Kilometer
    // sind. Beachte dass auch die Daten im Mietfahrzeug aktualisiert
    // werden müssen.
    // liefert true bei Erfolg, false bei einem Fehler
    public boolean returnCar(Rental r, Station returnStation,
                             LocalDate returnDate, Integer km) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        r.setReturnDate(returnDate);
        r.setReturnStation(returnStation);
        r.setKm(km);

        try {
            tx.begin();
            em.merge(r);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public void close() {
        JPAUtil.close();
    }

}