package dictionary.facade;

import dictionary.entity.ViewSymbol;
import dictionary.entity.ViewSymbol;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ViewSymbolFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public void create(ViewSymbol viewSymbol) {
        em.persist(viewSymbol);
    }

    public void update(ViewSymbol viewSymbol) {
        em.merge(viewSymbol);
    }

    public void remove(ViewSymbol viewSymbol) {
        em.remove(em.merge(viewSymbol));
    }

    public ViewSymbol findOne(int id) {
        String jpql = "SELECT vs FROM ViewSymbol vs WHERE vs.id = :vsId";
        TypedQuery<ViewSymbol> query = em.createQuery(jpql, ViewSymbol.class);
        query.setParameter("vsId", id);
        ViewSymbol viewSymbol = query.getSingleResult();

        return viewSymbol;
    }
    
    public List<ViewSymbol> findAll() {
        String jpql = "SELECT m FROM ViewSymbol m";
        TypedQuery<ViewSymbol> query = em.createQuery(jpql, ViewSymbol.class);
        List<ViewSymbol> meaningList = query.getResultList();

        return meaningList;
    }
}
