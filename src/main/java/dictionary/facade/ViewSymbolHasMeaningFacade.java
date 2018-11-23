package dictionary.facade;

import dictionary.entity.ViewSymbolHasMeaning;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ViewSymbolHasMeaningFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public void create(ViewSymbolHasMeaning viewSymbolHasMeaning) {
        em.persist(viewSymbolHasMeaning);
    }

    public void update(ViewSymbolHasMeaning viewSymbolHasMeaning) {
        em.merge(viewSymbolHasMeaning);
    }

    public void remove(ViewSymbolHasMeaning viewSymbolHasMeaning) {
        em.remove(em.merge(viewSymbolHasMeaning));
    }
}
