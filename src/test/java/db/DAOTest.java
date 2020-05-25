package db;

import model.Car;
import model.Customer;
import model.Rental;
import model.Station;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michael König
 */
public class DAOTest {
    private static DAO dao;

    @BeforeAll
    static void beforeAll() {
        dao = DAO.getINSTANCE();
        System.out.println(dao.insertTestData());
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        long newSize = em.createQuery("select count(s) from Station s", Long.class).getSingleResult();
        System.out.println(newSize);
        em.close();
    }

    /**
     * Inserts a new station and expects the count of all stations to rise by one.
     */
    @Test
    void testInsertStation1() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        long oldSize = em.createQuery("select count(s) from Station s", Long.class).getSingleResult();
        dao.insertStation(new Station("Tulln"));
        long newSize = em.createQuery("select count(s) from Station s", Long.class).getSingleResult();
        em.close();
        assertEquals(newSize, oldSize + 1);
    }


    /**
     * Attempts to create invalid entity and expects null to be returned
     */
    @Test
    void testInsertStation2() {
        assertFalse(dao.insertStation(null));
    }

    /**
     * Inserts a new customer and expects the count of all customers to rise by one.
     */
    @Test
    void testInsertCustomer1() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        long oldSize = em.createQuery("select count(c) from Customer c", Long.class).getSingleResult();
        dao.insertCustomer(new Customer(777777, "Florian", "Schlichting"));
        long newSize = em.createQuery("select count(c) from Customer c", Long.class).getSingleResult();
        em.close();
        assertEquals(newSize, oldSize + 1);
    }


    /**
     * Attempts to create invalid entity and expects null to be returned
     */
    @Test
    void testInsertCustomer2() {
        assertFalse(dao.insertCustomer(null));
    }

    /**
     * Inserts a new car and expects the count of all cars to rise by one.
     */
    @Test
    void testInsertCar1() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        long oldSize = em.createQuery("select count(c) from Car c", Long.class).getSingleResult();
        dao.insertCar(new Car("1234", 2000, 1000, "Model 1234", null, null));
        long newSize = em.createQuery("select count(c) from Car c", Long.class).getSingleResult();
        em.close();
        assertEquals(newSize, oldSize + 1);
    }

    /**
     * Attempts to create invalid entity and expects null to be returned
     */
    @Test
    void testInsertCar2() {
        assertFalse(dao.insertCar(null));
    }

    /**
     * Inserts a new rental and expects the count of all rentals to rise by one.
     */
    @Test
    void testInsertRental1() {
        Customer koenig = dao.findCustomer(111111);
        Car car5 = dao.findCar("5");
        Station krems = dao.findStationByCity("Krems");
        Station wien = dao.findStationByCity("Wien");

        long oldSize = dao.findRentalsByCustomer(koenig).size();
        dao.insertRental(new Rental(50, LocalDate.of(2020, 5, 10), LocalDate.of(2020, 5, 20), krems, wien, car5, koenig));
        long newSize = dao.findRentalsByCustomer(koenig).size();
        assertEquals(newSize, oldSize + 1);
    }

    /**
     * Attempts to create invalid entity and expects null to be returned
     */
    @Test
    void testInsertRental2() {
        assertFalse(dao.insertRental(null));
    }

    /**
     * Gets total number of cars in St. Pölten and expects it to be one.
     */
    @Test
    void testFindCarsByStation1() {
        Station stp = dao.findStationByCity("St. Pölten");
        assertEquals(1, dao.findCarsByStation(stp).size());
    }

    /**
     * Attempts to find cars from Station null
     * Expects IllegalArgumentException
     */
    @Test
    void testFindCarsByStation2() {
        assertThrows(IllegalArgumentException.class, () -> dao.findCarsByStation(null));
    }

    /**
     * Gets total number of rental from customer 111113 and expects it to be one.
     */
    @Test
    void testFindRentalsByCustomer1() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        Customer zach = dao.findCustomer(111113);

        em.close();
        assertEquals(1, dao.findRentalsByCustomer(zach).size());
    }

    /**
     * Attempts to find rentals from Customer null
     * Expects IllegalArgumentException
     */
    @Test
    void testFindRentalsByCustomer2() {
        assertThrows(IllegalArgumentException.class, () -> dao.findRentalsByCustomer(null));
    }

    /**
     * Returns a car and expects the mileage to rise by the driven distance
     */
    @Test
    void testReturnCar1() {
        Customer zach = dao.findCustomer(111113);
        Car car5 = dao.findCar("5");
        Station krems = dao.findStationByCity("Krems");
        Rental rental = dao.findRentalsByCustomer(zach).get(0);
        int km = rental.getCar().getMilage();
        dao.returnCar(rental, krems, LocalDate.of(2020, 05, 05), 30);
        assertEquals(km + 30, rental.getCar().getMilage());
    }

    /**
     * Attemps to complete already completed rental
     */
    @Test
    void testReturnCar2() {
        Customer koenig = dao.findCustomer(111111);
        Station krems = dao.findStationByCity("Krems");
        Rental rental = dao.findRentalsByCustomer(koenig).get(0);
        assertFalse(dao.returnCar(rental, krems, LocalDate.of(2020, 05, 05), 30));
    }


    @AfterAll
    static void afterAll() {
        dao.close();
    }

}
