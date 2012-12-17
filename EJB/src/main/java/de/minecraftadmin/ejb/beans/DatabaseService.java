package de.minecraftadmin.ejb.beans;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author BADMAN152
 * Represent database access
 */
@EJB
public class DatabaseService {

    @PersistenceContext(unitName = "Database",type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    /**
     * @author BADMAN152
     * save the data to the database
     * @param data
     */
    public void persist(Object data){
        entityManager.persist(data);
    }

    /**
     * @author BADMAN152
     * update detached data from database
     * for internal use only
     * @param data
     * @param <T>
     * @return
     */
    public <T> T update(T data){
        return entityManager.merge(data);
    }

    /**
     * @author BADMAN152
     * refresh data already persisted data
     * @param data
     */
    public void refresh(Object data){
        entityManager.refresh(data);
    }

    /**
     * @author BADMAN152
     * delete data from database
     * @param data
     */
    public void delete(Object data){
        entityManager.remove(data);
    }

    /**
     * @author BADMAN152
     * returns the list of objects from the database
     * @param clazz requested ClassType
     * @param sql the sql statement that match that requested clazz
     * @param args the list of arguments to prevent sql injection etc
     * @param <T> generic method type
     * @return list of mapped objects
     */
    public <T> List<T> getResultList(Class<T> clazz, String sql, Object[] args){
        TypedQuery<T> query = entityManager.createQuery(sql, clazz);
        for(int i=0;i<args.length;i++){
            query = query.setParameter(i,args);
        }
        return query.getResultList();
    }

    /**
     * @author BADMAN152
     * returns the single result
     * @param clazz requested ClassType
     * @param sql the sql statement that match that requested clazz
     * @param args the list of arguments to prevent sql injection etc
     * @param <T> generic method type
     * @return mapped object
     */
    public <T> T getSingleResult(Class<T> clazz,String sql,Object[] args){
        TypedQuery<T> query = entityManager.createQuery(sql, clazz);
        for(int i=0;i<args.length;i++){
            query = query.setParameter(i,args);
        }
        return query.getSingleResult();
    }
}
