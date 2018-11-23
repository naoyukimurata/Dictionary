package dictionary.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "view_symbol_has_meaning", catalog = "dictionary")
public class ViewSymbolHasMeaning implements java.io.Serializable {

	private ViewSymbolHasMeaningId id;
	private Meaning meaning;
	private ViewSymbol viewSymbol;

	public ViewSymbolHasMeaning() {
	}

	public ViewSymbolHasMeaning(ViewSymbolHasMeaningId id, Meaning meaning,
			ViewSymbol viewSymbol) {
		this.id = id;
		this.meaning = meaning;
		this.viewSymbol = viewSymbol;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "viewSymbolId", column = @Column(name = "view_symbol_id", nullable = false)),
			@AttributeOverride(name = "meaningId", column = @Column(name = "meaning_id", nullable = false))})
	public ViewSymbolHasMeaningId getId() {
		return this.id;
	}

	public void setId(ViewSymbolHasMeaningId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "meaning_id", nullable = false, insertable = false, updatable = false)
	public Meaning getMeaning() {
		return this.meaning;
	}

	public void setMeaning(Meaning meaning) {
		this.meaning = meaning;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "view_symbol_id", nullable = false, insertable = false, updatable = false)
	public ViewSymbol getViewSymbol() {
		return this.viewSymbol;
	}

	public void setViewSymbol(ViewSymbol viewSymbol) {
		this.viewSymbol = viewSymbol;
	}

}
