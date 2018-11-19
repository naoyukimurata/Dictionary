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

import dictionary.entity.Meaning;
import dictionary.entity.Clarifier;

/**
 * Backing bean for Meaning entities.
 * <p/>
 * This class provides CRUD functionality for all Meaning entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MeaningBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Meaning entities
	 */

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Meaning meaning;

	public Meaning getMeaning() {
		return this.meaning;
	}

	public void setMeaning(Meaning meaning) {
		this.meaning = meaning;
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
			this.meaning = this.example;
		} else {
			this.meaning = findById(getId());
		}
	}

	public Meaning findById(Integer id) {

		return this.entityManager.find(Meaning.class, id);
	}

	/*
	 * Support updating and deleting Meaning entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.meaning);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.meaning);
				return "view?faces-redirect=true&id=" + this.meaning.getId();
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
			Meaning deletableEntity = findById(getId());
			Clarifier clarifier = deletableEntity.getClarifier();
			clarifier.getMeanings().remove(deletableEntity);
			deletableEntity.setClarifier(null);
			this.entityManager.merge(clarifier);
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
	 * Support searching Meaning entities with pagination
	 */

	private int page;
	private long count;
	private List<Meaning> pageItems;

	private Meaning example = new Meaning();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Meaning getExample() {
		return this.example;
	}

	public void setExample(Meaning example) {
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
		Root<Meaning> root = countCriteria.from(Meaning.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Meaning> criteria = builder.createQuery(Meaning.class);
		root = criteria.from(Meaning.class);
		TypedQuery<Meaning> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Meaning> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Clarifier clarifier = this.example.getClarifier();
		if (clarifier != null) {
			predicatesList.add(builder.equal(root.get("clarifier"), clarifier));
		}
		String word = this.example.getWord();
		if (word != null && !"".equals(word)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("word")),
					'%' + word.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Meaning> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Meaning entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Meaning> getAll() {

		CriteriaQuery<Meaning> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Meaning.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Meaning.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final MeaningBean ejbProxy = this.sessionContext
				.getBusinessObject(MeaningBean.class);

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

				return String.valueOf(((Meaning) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Meaning add = new Meaning();

	public Meaning getAdd() {
		return this.add;
	}

	public Meaning getAdded() {
		Meaning added = this.add;
		this.add = new Meaning();
		return added;
	}
}
