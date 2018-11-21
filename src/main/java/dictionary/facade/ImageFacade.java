package dictionary.facade;

import dictionary.entity.Image;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ImageFacade {
    @PersistenceContext(unitName = "Dictionary-persistence-unit")
    private EntityManager em;

    public void create(Image image) {
      em.persist(image);
    }

    public void update(Image image) {
        em.merge(image);
    }
}
