package dictionary.entity;
// Generated 2018/11/20 23:16:32 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SymbolGraphic generated by hbm2java
 */
@Entity
@Table(name = "symbol_graphic", catalog = "dictionary")
public class SymbolGraphic implements java.io.Serializable {

	private Integer id;
	private MultiviewSymbol multiviewSymbol;
	private String name;

	public SymbolGraphic() {
	}

	public SymbolGraphic(MultiviewSymbol multiviewSymbol, String name) {
		this.multiviewSymbol = multiviewSymbol;
		this.name = name;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "multiview_symbol_id", nullable = false)
	public MultiviewSymbol getMultiviewSymbol() {
		return this.multiviewSymbol;
	}

	public void setMultiviewSymbol(MultiviewSymbol multiviewSymbol) {
		this.multiviewSymbol = multiviewSymbol;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
