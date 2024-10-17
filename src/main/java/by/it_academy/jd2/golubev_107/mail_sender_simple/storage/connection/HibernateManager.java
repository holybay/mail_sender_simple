package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Function;

public class HibernateManager implements AutoCloseable {

    private final EntityManagerFactory factory;

    public HibernateManager(String unitName) {
        this.factory = Persistence.createEntityManagerFactory(unitName);
    }

    public <T> T inTransaction(Function<EntityManager, T> work) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T data = work.apply(entityManager);
            transaction.commit();
            return data;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Rolling back the transaction!", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void close() throws Exception {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
