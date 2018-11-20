package dictionary.facade;

import dictionary.entity.MultiviewSymbol;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class MultiviewSymbolFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public Boolean create(MultiviewSymbol multiviewSymbol) {
        if(checkOverlap(multiviewSymbol)) {
            em.persist(multiviewSymbol);
            return true;
        } else return false;
    }

    public boolean update(MultiviewSymbol multiviewSymbol) {
        if(checkOverlap(multiviewSymbol)) {
            em.merge(multiviewSymbol);
            return true;
        } else return false;
    }

    public boolean checkOverlap(MultiviewSymbol multiviewSymbol) {
        String jpql = "SELECT ms FROM MultiviewSymbol ms WHERE ms.caption = :msCaption";
        TypedQuery<MultiviewSymbol> query = em.createQuery(jpql, MultiviewSymbol.class);
        query.setParameter("msCaption", multiviewSymbol.getCaption());
        try {
            query.getSingleResult();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
