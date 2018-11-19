package dictionary.entity;
// Generated 2018/11/20 3:12:54 by Hibernate Tools 4.3.1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ViewSymbolHasClarifier generated by hbm2java
 */
@Entity
@Table(name = "view_symbol_has_clarifier", catalog = "dictionary")
public class ViewSymbolHasClarifier implements java.io.Serializable {

	private ViewSymbolHasClarifierId id;
	private Clarifier clarifier;
	private ViewSymbol viewSymbol;

	public ViewSymbolHasClarifier() {
	}

	public ViewSymbolHasClarifier(ViewSymbolHasClarifierId id,
			Clarifier clarifier, ViewSymbol viewSymbol) {
		this.id = id;
		this.clarifier = clarifier;
		this.viewSymbol = viewSymbol;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "viewSymbolId", column = @Column(name = "view_symbol_id", nullable = false)),
			@AttributeOverride(name = "clarifierId", column = @Column(name = "clarifier_id", nullable = false))})
	public ViewSymbolHasClarifierId getId() {
		return this.id;
	}

	public void setId(ViewSymbolHasClarifierId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clarifier_id", nullable = false, insertable = false, updatable = false)
	public Clarifier getClarifier() {
		return this.clarifier;
	}

	public void setClarifier(Clarifier clarifier) {
		this.clarifier = clarifier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "view_symbol_id", nullable = false, insertable = false, updatable = false)
	public ViewSymbol getViewSymbol() {
		return this.viewSymbol;
	}

	public void setViewSymbol(ViewSymbol viewSymbol) {
		this.viewSymbol = viewSymbol;
	}

}
