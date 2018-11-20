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

import dictionary.entity.MultiviewSymbol;
import dictionary.entity.SymbolGraphic;
import dictionary.entity.ViewSymbol;
import java.util.Iterator;

/**
 * Backing bean for MultiviewSymbol entities.
 * <p/>
 * This class provides CRUD functionality for all MultiviewSymbol entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MultiviewSymbolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving MultiviewSymbol entities
	 */

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private MultiviewSymbol multiviewSymbol;

	public MultiviewSymbol getMultiviewSymbol() {
		return this.multiviewSymbol;
	}

	public void setMultiviewSymbol(MultiviewSymbol multiviewSymbol) {
		this.multiviewSymbol = multiviewSymbol;
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
			this.multiviewSymbol = this.example;
		} else {
			this.multiviewSymbol = findById(getId());
		}
	}

	public MultiviewSymbol findById(Integer id) {

		return this.entityManager.find(MultiviewSymbol.class, id);
	}

	/*
	 * Support updating and deleting MultiviewSymbol entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.multiviewSymbol);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.multiviewSymbol);
				return "view?faces-redirect=true&id="
						+ this.multiviewSymbol.getId();
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
			MultiviewSymbol deletableEntity = findById(getId());
			Iterator<ViewSymbol> iterViewSymbols = deletableEntity
					.getViewSymbols().iterator();
			for (; iterViewSymbols.hasNext();) {
				ViewSymbol nextInViewSymbols = iterViewSymbols.next();
				nextInViewSymbols.setMultiviewSymbol(null);
				iterViewSymbols.remove();
				this.entityManager.merge(nextInViewSymbols);
			}
			Iterator<SymbolGraphic> iterSymbolGraphics = deletableEntity
					.getSymbolGraphics().iterator();
			for (; iterSymbolGraphics.hasNext();) {
				SymbolGraphic nextInSymbolGraphics = iterSymbolGraphics.next();
				nextInSymbolGraphics.setMultiviewSymbol(null);
				iterSymbolGraphics.remove();
				this.entityManager.merge(nextInSymbolGraphics);
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
	 * Support searching MultiviewSymbol entities with pagination
	 */

	private int page;
	private long count;
	private List<MultiviewSymbol> pageItems;

	private MultiviewSymbol example = new MultiviewSymbol();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public MultiviewSymbol getExample() {
		return this.example;
	}

	public void setExample(MultiviewSymbol example) {
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
		Root<MultiviewSymbol> root = countCriteria.from(MultiviewSymbol.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<MultiviewSymbol> criteria = builder
				.createQuery(MultiviewSymbol.class);
		root = criteria.from(MultiviewSymbol.class);
		TypedQuery<MultiviewSymbol> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<MultiviewSymbol> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String caption = this.example.getCaption();
		if (caption != null && !"".equals(caption)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("caption")),
					'%' + caption.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<MultiviewSymbol> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back MultiviewSymbol entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<MultiviewSymbol> getAll() {

		CriteriaQuery<MultiviewSymbol> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(MultiviewSymbol.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(MultiviewSymbol.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final MultiviewSymbolBean ejbProxy = this.sessionContext
				.getBusinessObject(MultiviewSymbolBean.class);

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

				return String.valueOf(((MultiviewSymbol) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private MultiviewSymbol add = new MultiviewSymbol();

	public MultiviewSymbol getAdd() {
		return this.add;
	}

	public MultiviewSymbol getAdded() {
		MultiviewSymbol added = this.add;
		this.add = new MultiviewSymbol();
		return added;
	}
}
