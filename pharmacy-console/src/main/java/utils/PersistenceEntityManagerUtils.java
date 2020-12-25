package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * Содержит методы для взаимодействия с {@code EntityManager}
 */
public class PersistenceEntityManagerUtils {
    private static final Logger logger = LogManager.getLogger(PersistenceEntityManagerUtils.class);
    private static EntityManager entityManager;

    private PersistenceEntityManagerUtils() {}

    /**
     * Возвращает инстанс EntityManаger'а.
     * Сперва создает его, если не существует
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        if (entityManager == null) {
                var emf =
                        Persistence.createEntityManagerFactory("pharmacy_persistence");
                entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    /**
     * Вызывает {@link #getEntityManager} и позвращает
     * {@code true} в случае удачного завершения вызова,
     * иначе возвращает {@code false}
     * @return {@code true} если метод завершился удачно, иначе {@code false}
     */
    public static boolean tryInitializeEntityManager() {
        try {
            getEntityManager();
            return true;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }

    /**
     * Устанавливает текущий инстанс EntityManager'a. Использует в тестах
     * @param entityManager Новый инстанс
     */
    public static void setEntityManager(EntityManager entityManager) {
        if (PersistenceEntityManagerUtils.entityManager != null) {
            PersistenceEntityManagerUtils.entityManager.close();
        }
        PersistenceEntityManagerUtils.entityManager = entityManager;
    }

    /**
     * Функция, выполняющая некоторые действия в контексте транзакции
     * @param <R> Тип возвращаемого значения из функции
     */
    public interface EntityManagerCallback<R> {
        R call(EntityManager entityManager);
    }

    /**
     * Открывает новую транзакции и вызывает {@code callback}
     * @param callback Функция, выполняющая действия с БД
     * @param <T> Тип возвращаемого значения из функции
     * @return Возвращает то, что вернет {@code callback}
     */
    public static <T> T doTransaction(EntityManagerCallback<T> callback) {
        var em = getEntityManager();
        em.getTransaction().begin();
        try {
            var res = callback.call(entityManager);
            em.getTransaction().commit();
            return res;
        } catch (Exception ex) {
            logger.error("Transaction failed", ex);
            em.getTransaction().rollback();
            throw ex;
        }
    }

    /**
     * Специализация {@link EntityManagerCallback} для void-функций
     */
    public interface EntityManagerVoidCallback {
        void call(EntityManager entityManager);
    }

    /**
     * Перегрузка {@link #doTransaction} для void-функций
     * @param callback void-функция, выполняющая действия с БД
     */
    public static void doTransaction(EntityManagerVoidCallback callback) {
        doTransaction((em) -> {
            callback.call(em);
            return null;
        });
    }
}
