package dictionary.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dictionary.entity.Image;
import dictionary.entity.ViewSymbol;
import java.util.Iterator;

/**
 * Backing bean for Image entities.
 * <p/>
 * This class provides CRUD functionality for all Image entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ImageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Image entities
	 */

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Image image;

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "Dictionary-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.image = this.example;
		} else {
			this.image = findById(getId());
		}
	}

	public Image findById(Integer id) {

		return this.entityManager.find(Image.class, id);
	}

	/*
	 * Support updating and deleting Image entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.image);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.image);
				return "view?faces-redirect=true&id=" + this.image.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Image deletableEntity = findById(getId());
			Iterator<ViewSymbol> iterViewSymbols = deletableEntity
					.getViewSymbols().iterator();
			for (; iterViewSymbols.hasNext();) {
				ViewSymbol nextInViewSymbols = iterViewSymbols.next();
				nextInViewSymbols.setImage(null);
				iterViewSymbols.remove();
				this.entityManager.merge(nextInViewSymbols);
			}
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Image entities with pagination
	 */

	private int page;
	private long count;
	private List<Image> pageItems;

	private Image example = new Image();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Image getExample() {
		return this.example;
	}

	public void setExample(Image example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Image> root = countCriteria.from(Image.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Image> criteria = builder.createQuery(Image.class);
		root = criteria.from(Image.class);
		TypedQuery<Image> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Image> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name")),
					'%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Image> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Image entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Image> getAll() {

		CriteriaQuery<Image> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(Image.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Image.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ImageBean ejbProxy = this.sessionContext
				.getBusinessObject(ImageBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
									  UIComponent component, String value) {

				return ejbProxy.findById(Integer.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
									  UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Image) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Image add = new Image();

	public Image getAdd() {
		return this.add;
	}

	public Image getAdded() {
		Image added = this.add;
		this.add = new Image();
		return added;
	}
}
