package dictionary.entity;
// Generated 2018/11/20 23:16:32 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "multiview_symbol", catalog = "dictionary")
public class MultiviewSymbol implements java.io.Serializable {

	private Integer id;
	private String caption;
	private Set<ViewSymbol> viewSymbols = new HashSet<ViewSymbol>(0);
	private Set<SymbolGraphic> symbolGraphics = new HashSet<SymbolGraphic>(0);

	public MultiviewSymbol() {
	}

	public MultiviewSymbol(String caption) {
		this.caption = caption;
	}
	public MultiviewSymbol(String caption, Set<ViewSymbol> viewSymbols,
			Set<SymbolGraphic> symbolGraphics) {
		this.caption = caption;
		this.viewSymbols = viewSymbols;
		this.symbolGraphics = symbolGraphics;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "caption", nullable = false, length = 45)
	public String getCaption() {
		return this.caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "multiviewSymbol")
	public Set<ViewSymbol> getViewSymbols() {
		return this.viewSymbols;
	}

	public void setViewSymbols(Set<ViewSymbol> viewSymbols) {
		this.viewSymbols = viewSymbols;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "multiviewSymbol")
	public Set<SymbolGraphic> getSymbolGraphics() {
		return this.symbolGraphics;
	}

	public void setSymbolGraphics(Set<SymbolGraphic> symbolGraphics) {
		this.symbolGraphics = symbolGraphics;
	}

}
