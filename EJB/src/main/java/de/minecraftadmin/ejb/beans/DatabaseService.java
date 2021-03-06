package de.minecraftadmin.ejb.beans;

import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BADMAN152
 *         Represent database access
 */
@Stateless
@Startup
public class DatabaseService {

    @PersistenceContext(unitName = "Database", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    /**
     * @param data
     * @author BADMAN152
     * save the data to the database
     */
    public void persist(Object data) {
        data = entityManager.merge(data);
        entityManager.persist(data);
    }

    /**
     * @param data
     * @param <T>
     * @return
     * @author BADMAN152
     * update detached data from database
     * for internal use only
     */
    public <T> T update(T data) {
        return entityManager.merge(data);
    }

    /**
     * @param data
     * @author BADMAN152
     * refresh data already persisted data
     */
    public void refresh(Object data) {
        entityManager.refresh(data);
    }

    /**
     * @param data
     * @author BADMAN152
     * delete data from database
     */
    public void delete(Object data) {
        data = entityManager.merge(data);
        entityManager.remove(data);
    }

    /**
     * @param clazz requested ClassType
     * @param sql   the sql statement that match that requested clazz
     * @param args  the list of arguments to prevent sql injection etc
     * @param <T>   generic method type
     * @return list of mapped objects
     * @author BADMAN152
     * returns the list of objects from the database
     */
    public <T> List<T> getResultList(Class<T> clazz, String sql, HashMap<String, Object> params) {
        return generateQuery(clazz, sql, params).getResultList();
    }

    public <T> List<T> getResultListWithLimit(Class<T> clazz, String sql, HashMap<String, Object> params, int startAt, int endAt) {
        TypedQuery<T> query = generateQuery(clazz, sql, params);
        query.setFirstResult(startAt);
        query.setMaxResults(endAt);
        return query.getResultList();
    }

    /**
     * @param clazz requested ClassType
     * @param sql   the sql statement that match that requested clazz
     * @param args  the list of arguments to prevent sql injection etc
     * @return mapped object
     * @author BADMAN152
     * returns the single result
     */
    public <T> T getSingleResult(Class<T> clazz, String sql, HashMap<String, Object> args) {
        try {
            return generateQuery(clazz, sql, args).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private <T> TypedQuery<T> generateQuery(Class<T> clazz, String sql, HashMap<String, Object> args) {
        TypedQuery<T> query = entityManager.createQuery(sql, clazz);
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            query = query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    public Object querySingeResult(String s, HashMap<String, Object> params) {
        Query query = entityManager.createQuery(s);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query = query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getSingleResult();
    }
}
