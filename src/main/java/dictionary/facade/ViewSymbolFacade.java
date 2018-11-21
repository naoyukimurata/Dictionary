package dictionary.facade;

import dictionary.entity.SymbolGraphic;
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

    public List<SymbolGraphic> findAll() {
        String jpql = "SELECT m FROM SymbolGraphic m";
        TypedQuery<SymbolGraphic> query = em.createQuery(jpql, SymbolGraphic.class);
        List<SymbolGraphic> meaningList = query.getResultList();

        return meaningList;
    }
}
