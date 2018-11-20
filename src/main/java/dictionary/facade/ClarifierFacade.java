package dictionary.facade;

import dictionary.entity.Clarifier;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClarifierFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public Boolean create(Clarifier clarifier) {
        if(checkOverlap(clarifier)) {
            em.persist(clarifier);
            return true;
        } else return false;
    }

    public boolean checkOverlap(Clarifier clarifier) {
        String jpql = "SELECT c FROM Clarifier c WHERE c.typeName = :cTypeName";
        TypedQuery<Clarifier> query = em.createQuery(jpql, Clarifier.class);
        query.setParameter("cTypeName", clarifier.getTypeName());
        try {
            query.getSingleResult();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
    
    public Clarifier findOne(int cId) {
        String jpql = "SELECT c FROM Clarifier c WHERE c.id = :cId";
        TypedQuery<Clarifier> query = em.createQuery(jpql, Clarifier.class);
        query.setParameter("cId", cId);
        Clarifier clarifier = query.getSingleResult();

        return clarifier;
    }
    
    public List<Clarifier> findAll() {
        String jpql = "SELECT c FROM Clarifier c";
        TypedQuery<Clarifier> query = em.createQuery(jpql, Clarifier.class);
        List<Clarifier> clarifierList = query.getResultList();

        return clarifierList;
    }
}
