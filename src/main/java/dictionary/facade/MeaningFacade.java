package dictionary.facade;

import dictionary.entity.Meaning;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MeaningFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public Boolean create(Meaning meaning) {
        if(checkOverlap(meaning)) {
            em.persist(meaning);
            return true;
        } else return false;
    }

    public boolean update(Meaning meaning) {
        if(checkOverlap(meaning)) {
            em.merge(meaning);
            return true;
        } else return false;
    }

    public boolean checkOverlap(Meaning meaning) {
        String jpql = "SELECT m FROM Meaning m WHERE m.word = :mWord";
        TypedQuery<Meaning> query = em.createQuery(jpql, Meaning.class);
        query.setParameter("mWord", meaning.getWord());
        try {
            query.getSingleResult();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public void remove(Meaning meaning) {
        em.remove(em.merge(meaning));
    }
    
    public Meaning findOne(int cId) {
        String jpql = "SELECT c FROM Meaning c WHERE c.id = :cId";
        TypedQuery<Meaning> query = em.createQuery(jpql, Meaning.class);
        query.setParameter("cId", cId);
        Meaning meaning = query.getSingleResult();

        return meaning;
    }
    
    public List<Meaning> findAll() {
        String jpql = "SELECT m FROM Meaning m";
        TypedQuery<Meaning> query = em.createQuery(jpql, Meaning.class);
        List<Meaning> meaningList = query.getResultList();

        return meaningList;
    }
}
