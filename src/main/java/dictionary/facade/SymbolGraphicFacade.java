package dictionary.facade;

import dictionary.entity.MultiviewSymbol;
import dictionary.entity.SymbolGraphic;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class SymbolGraphicFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public void create(SymbolGraphic symbolGraphic) {
        em.persist(symbolGraphic);
    }

    public void update(SymbolGraphic symbolGraphic) {
        em.merge(symbolGraphic);
    }

    public List<SymbolGraphic> findAll() {
        String jpql = "SELECT m FROM SymbolGraphic m";
        TypedQuery<SymbolGraphic> query = em.createQuery(jpql, SymbolGraphic.class);
        List<SymbolGraphic> meaningList = query.getResultList();

        return meaningList;
    }
}
