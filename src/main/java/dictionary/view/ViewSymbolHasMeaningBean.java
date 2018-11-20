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

import dictionary.entity.ViewSymbolHasMeaning;
import dictionary.entity.Meaning;
import dictionary.entity.ViewSymbol;

/**
 * Backing bean for ViewSymbolHasMeaning entities.
 * <p/>
 * This class provides CRUD functionality for all ViewSymbolHasMeaning entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ViewSymbolHasMeaningBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ViewSymbolHasMeaning entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ViewSymbolHasMeaning viewSymbolHasMeaning;

	public ViewSymbolHasMeaning getViewSymbolHasMeaning() {
		return this.viewSymbolHasMeaning;
	}

	public void setViewSymbolHasMeaning(
			ViewSymbolHasMeaning viewSymbolHasMeaning) {
		this.viewSymbolHasMeaning = viewSymbolHasMeaning;
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
			this.viewSymbolHasMeaning = this.example;
		} else {
			this.viewSymbolHasMeaning = findById(getId());
		}
	}

	public ViewSymbolHasMeaning findById(Long id) {

		return this.entityManager.find(ViewSymbolHasMeaning.class, id);
	}

	/*
	 * Support updating and deleting ViewSymbolHasMeaning entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.viewSymbolHasMeaning);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.viewSymbolHasMeaning);
				return "view?faces-redirect=true&id="
						+ this.viewSymbolHasMeaning.getId();
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
			ViewSymbolHasMeaning deletableEntity = findById(getId());
			Meaning meaning = deletableEntity.getMeaning();
			meaning.getViewSymbolHasMeanings().remove(deletableEntity);
			deletableEntity.setMeaning(null);
			this.entityManager.merge(meaning);
			ViewSymbol viewSymbol = deletableEntity.getViewSymbol();
			viewSymbol.getViewSymbolHasMeanings().remove(deletableEntity);
			deletableEntity.setViewSymbol(null);
			this.entityManager.merge(viewSymbol);
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
	 * Support searching ViewSymbolHasMeaning entities with pagination
	 */

	private int page;
	private long count;
	private List<ViewSymbolHasMeaning> pageItems;

	private ViewSymbolHasMeaning example = new ViewSymbolHasMeaning();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ViewSymbolHasMeaning getExample() {
		return this.example;
	}

	public void setExample(ViewSymbolHasMeaning example) {
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
		Root<ViewSymbolHasMeaning> root = countCriteria
				.from(ViewSymbolHasMeaning.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ViewSymbolHasMeaning> criteria = builder
				.createQuery(ViewSymbolHasMeaning.class);
		root = criteria.from(ViewSymbolHasMeaning.class);
		TypedQuery<ViewSymbolHasMeaning> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ViewSymbolHasMeaning> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Meaning meaning = this.example.getMeaning();
		if (meaning != null) {
			predicatesList.add(builder.equal(root.get("meaning"), meaning));
		}
		ViewSymbol viewSymbol = this.example.getViewSymbol();
		if (viewSymbol != null) {
			predicatesList
					.add(builder.equal(root.get("viewSymbol"), viewSymbol));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ViewSymbolHasMeaning> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ViewSymbolHasMeaning entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<ViewSymbolHasMeaning> getAll() {

		CriteriaQuery<ViewSymbolHasMeaning> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ViewSymbolHasMeaning.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ViewSymbolHasMeaning.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ViewSymbolHasMeaningBean ejbProxy = this.sessionContext
				.getBusinessObject(ViewSymbolHasMeaningBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((ViewSymbolHasMeaning) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ViewSymbolHasMeaning add = new ViewSymbolHasMeaning();

	public ViewSymbolHasMeaning getAdd() {
		return this.add;
	}

	public ViewSymbolHasMeaning getAdded() {
		ViewSymbolHasMeaning added = this.add;
		this.add = new ViewSymbolHasMeaning();
		return added;
	}
}
