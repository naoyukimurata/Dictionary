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

import dictionary.entity.ViewSymbolHasImage;
import dictionary.entity.Image;
import dictionary.entity.ViewSymbol;

/**
 * Backing bean for ViewSymbolHasImage entities.
 * <p/>
 * This class provides CRUD functionality for all ViewSymbolHasImage entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ViewSymbolHasImageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ViewSymbolHasImage entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ViewSymbolHasImage viewSymbolHasImage;

	public ViewSymbolHasImage getViewSymbolHasImage() {
		return this.viewSymbolHasImage;
	}

	public void setViewSymbolHasImage(ViewSymbolHasImage viewSymbolHasImage) {
		this.viewSymbolHasImage = viewSymbolHasImage;
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
			this.viewSymbolHasImage = this.example;
		} else {
			this.viewSymbolHasImage = findById(getId());
		}
	}

	public ViewSymbolHasImage findById(Long id) {

		return this.entityManager.find(ViewSymbolHasImage.class, id);
	}

	/*
	 * Support updating and deleting ViewSymbolHasImage entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.viewSymbolHasImage);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.viewSymbolHasImage);
				return "view?faces-redirect=true&id="
						+ this.viewSymbolHasImage.getId();
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
			ViewSymbolHasImage deletableEntity = findById(getId());
			Image image = deletableEntity.getImage();
			image.getViewSymbolHasImages().remove(deletableEntity);
			deletableEntity.setImage(null);
			this.entityManager.merge(image);
			ViewSymbol viewSymbol = deletableEntity.getViewSymbol();
			viewSymbol.getViewSymbolHasImages().remove(deletableEntity);
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
	 * Support searching ViewSymbolHasImage entities with pagination
	 */

	private int page;
	private long count;
	private List<ViewSymbolHasImage> pageItems;

	private ViewSymbolHasImage example = new ViewSymbolHasImage();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ViewSymbolHasImage getExample() {
		return this.example;
	}

	public void setExample(ViewSymbolHasImage example) {
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
		Root<ViewSymbolHasImage> root = countCriteria
				.from(ViewSymbolHasImage.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ViewSymbolHasImage> criteria = builder
				.createQuery(ViewSymbolHasImage.class);
		root = criteria.from(ViewSymbolHasImage.class);
		TypedQuery<ViewSymbolHasImage> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ViewSymbolHasImage> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Image image = this.example.getImage();
		if (image != null) {
			predicatesList.add(builder.equal(root.get("image"), image));
		}
		ViewSymbol viewSymbol = this.example.getViewSymbol();
		if (viewSymbol != null) {
			predicatesList
					.add(builder.equal(root.get("viewSymbol"), viewSymbol));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ViewSymbolHasImage> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ViewSymbolHasImage entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<ViewSymbolHasImage> getAll() {

		CriteriaQuery<ViewSymbolHasImage> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ViewSymbolHasImage.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ViewSymbolHasImage.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ViewSymbolHasImageBean ejbProxy = this.sessionContext
				.getBusinessObject(ViewSymbolHasImageBean.class);

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

				return String.valueOf(((ViewSymbolHasImage) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ViewSymbolHasImage add = new ViewSymbolHasImage();

	public ViewSymbolHasImage getAdd() {
		return this.add;
	}

	public ViewSymbolHasImage getAdded() {
		ViewSymbolHasImage added = this.add;
		this.add = new ViewSymbolHasImage();
		return added;
	}
}
