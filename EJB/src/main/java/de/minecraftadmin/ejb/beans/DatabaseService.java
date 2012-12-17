package de.minecraftadmin.ejb.beans;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

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


}
