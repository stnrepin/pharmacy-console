package utils;

import org.postgresql.util.PSQLException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class PersistenceEntityManagerUtils {
    private static EntityManager entityManager;

    private PersistenceEntityManagerUtils() {}

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
                var emf =
                        Persistence.createEntityManagerFactory("pharmacy_persistence");
                entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    public static boolean tryInitializeEntityManager() {
        try {
            getEntityManager();
            return true;
        } catch (Exception e) {
            System.out.println("tryInitializeEntityManager() [Error] " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public interface EntityManagerCallback<R> {
        R call(EntityManager entityManager);
    }

    public static <T> T doTransaction(EntityManagerCallback<T> callback) {
        var em = getEntityManager();
        em.getTransaction().begin();
        try {
            var res = callback.call(entityManager);
            em.getTransaction().commit();
            return res;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    public interface EntityManagerVoidCallback {
        void call(EntityManager entityManager);
    }

    public static void doTransaction(EntityManagerVoidCallback callback) {
        doTransaction((em) -> {
            callback.call(em);
            return null;
        });
    }
}
