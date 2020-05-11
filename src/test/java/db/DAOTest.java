package db;

import model.Car;
import model.Customer;
import model.Station;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

/**
 * @author Michael König
 */
public class DAOTest {
    private static DAO dao;

    @BeforeAll
    static void beforeAll() {
        dao = DAO.getINSTANCE();
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        dao.insertTestData();
    }

    @Test
    void dummy() {
    }

    @AfterAll
    static void afterAll() {
        dao.close();
    }

}
