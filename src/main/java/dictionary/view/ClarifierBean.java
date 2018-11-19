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

import dictionary.entity.Clarifier;
import dictionary.entity.Meaning;
import dictionary.entity.ViewSymbolHasClarifier;
import java.util.Iterator;

/**
 * Backing bean for Clarifier entities.
 * <p/>
 * This class provides CRUD functionality for all Clarifier entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ClarifierBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Clarifier entities
	 */

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Clarifier clarifier;

	public Clarifier getClarifier() {
		return this.clarifier;
	}

	public void setClarifier(Clarifier clarifier) {
		this.clarifier = clarifier;
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
			this.clarifier = this.example;
		} else {
			this.clarifier = findById(getId());
		}
	}

	public Clarifier findById(Integer id) {

		return this.entityManager.find(Clarifier.class, id);
	}

	/*
	 * Support updating and deleting Clarifier entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.clarifier);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.clarifier);
				return "view?faces-redirect=true&id=" + this.clarifier.getId();
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
			Clarifier deletableEntity = findById(getId());
			Iterator<Meaning> iterMeanings = deletableEntity.getMeanings()
					.iterator();
			for (; iterMeanings.hasNext();) {
				Meaning nextInMeanings = iterMeanings.next();
				nextInMeanings.setClarifier(null);
				iterMeanings.remove();
				this.entityManager.merge(nextInMeanings);
			}
			Iterator<ViewSymbolHasClarifier> iterViewSymbolHasClarifiers = deletableEntity
					.getViewSymbolHasClarifiers().iterator();
			for (; iterViewSymbolHasClarifiers.hasNext();) {
				ViewSymbolHasClarifier nextInViewSymbolHasClarifiers = iterViewSymbolHasClarifiers
						.next();
				nextInViewSymbolHasClarifiers.setClarifier(null);
				iterViewSymbolHasClarifiers.remove();
				this.entityManager.merge(nextInViewSymbolHasClarifiers);
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
	 * Support searching Clarifier entities with pagination
	 */

	private int page;
	private long count;
	private List<Clarifier> pageItems;

	private Clarifier example = new Clarifier();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Clarifier getExample() {
		return this.example;
	}

	public void setExample(Clarifier example) {
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
		Root<Clarifier> root = countCriteria.from(Clarifier.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Clarifier> criteria = builder
				.createQuery(Clarifier.class);
		root = criteria.from(Clarifier.class);
		TypedQuery<Clarifier> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Clarifier> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String typeName = this.example.getTypeName();
		if (typeName != null && !"".equals(typeName)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("typeName")),
					'%' + typeName.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Clarifier> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Clarifier entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Clarifier> getAll() {

		CriteriaQuery<Clarifier> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Clarifier.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Clarifier.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ClarifierBean ejbProxy = this.sessionContext
				.getBusinessObject(ClarifierBean.class);

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

				return String.valueOf(((Clarifier) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Clarifier add = new Clarifier();

	public Clarifier getAdd() {
		return this.add;
	}

	public Clarifier getAdded() {
		Clarifier added = this.add;
		this.add = new Clarifier();
		return added;
	}
}
