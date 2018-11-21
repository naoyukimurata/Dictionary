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

import dictionary.entity.ViewSymbol;
import dictionary.entity.Image;
import dictionary.entity.MultiviewSymbol;
import dictionary.entity.ViewSymbolHasMeaning;
import java.util.Iterator;

/**
 * Backing bean for ViewSymbol entities.
 * <p/>
 * This class provides CRUD functionality for all ViewSymbol entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ViewSymbolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ViewSymbol entities
	 */

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private ViewSymbol viewSymbol;

	public ViewSymbol getViewSymbol() {
		return this.viewSymbol;
	}

	public void setViewSymbol(ViewSymbol viewSymbol) {
		this.viewSymbol = viewSymbol;
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
			this.viewSymbol = this.example;
		} else {
			this.viewSymbol = findById(getId());
		}
	}

	public ViewSymbol findById(Integer id) {

		return this.entityManager.find(ViewSymbol.class, id);
	}

	/*
	 * Support updating and deleting ViewSymbol entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.viewSymbol);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.viewSymbol);
				return "view?faces-redirect=true&id=" + this.viewSymbol.getId();
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
			ViewSymbol deletableEntity = findById(getId());
			Image image = deletableEntity.getImage();
			image.getViewSymbols().remove(deletableEntity);
			deletableEntity.setImage(null);
			this.entityManager.merge(image);
			MultiviewSymbol multiviewSymbol = deletableEntity
					.getMultiviewSymbol();
			multiviewSymbol.getViewSymbols().remove(deletableEntity);
			deletableEntity.setMultiviewSymbol(null);
			this.entityManager.merge(multiviewSymbol);
			Iterator<ViewSymbolHasMeaning> iterViewSymbolHasMeanings = deletableEntity
					.getViewSymbolHasMeanings().iterator();
			for (; iterViewSymbolHasMeanings.hasNext();) {
				ViewSymbolHasMeaning nextInViewSymbolHasMeanings = iterViewSymbolHasMeanings
						.next();
				nextInViewSymbolHasMeanings.setViewSymbol(null);
				iterViewSymbolHasMeanings.remove();
				this.entityManager.merge(nextInViewSymbolHasMeanings);
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
	 * Support searching ViewSymbol entities with pagination
	 */

	private int page;
	private long count;
	private List<ViewSymbol> pageItems;

	private ViewSymbol example = new ViewSymbol();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ViewSymbol getExample() {
		return this.example;
	}

	public void setExample(ViewSymbol example) {
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
		Root<ViewSymbol> root = countCriteria.from(ViewSymbol.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ViewSymbol> criteria = builder
				.createQuery(ViewSymbol.class);
		root = criteria.from(ViewSymbol.class);
		TypedQuery<ViewSymbol> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ViewSymbol> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Image image = this.example.getImage();
		if (image != null) {
			predicatesList.add(builder.equal(root.get("image"), image));
		}
		MultiviewSymbol multiviewSymbol = this.example.getMultiviewSymbol();
		if (multiviewSymbol != null) {
			predicatesList.add(builder.equal(root.get("multiviewSymbol"),
					multiviewSymbol));
		}
		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name")),
					'%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ViewSymbol> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ViewSymbol entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<ViewSymbol> getAll() {

		CriteriaQuery<ViewSymbol> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ViewSymbol.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ViewSymbol.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ViewSymbolBean ejbProxy = this.sessionContext
				.getBusinessObject(ViewSymbolBean.class);

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

				return String.valueOf(((ViewSymbol) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ViewSymbol add = new ViewSymbol();

	public ViewSymbol getAdd() {
		return this.add;
	}

	public ViewSymbol getAdded() {
		ViewSymbol added = this.add;
		this.add = new ViewSymbol();
		return added;
	}
}
