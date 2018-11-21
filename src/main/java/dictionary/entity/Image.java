package dictionary.entity;
// Generated 2018/11/22 6:27:04 by Hibernate Tools 4.3.1

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
@Table(name = "image", catalog = "dictionary")
public class Image implements java.io.Serializable {

	private Integer id;
	private String name;
	private Set<ViewSymbol> viewSymbols = new HashSet<ViewSymbol>(0);

	public Image() {
	}

	public Image(String name) {
		this.name = name;
	}
	public Image(String name, Set<ViewSymbol> viewSymbols) {
		this.name = name;
		this.viewSymbols = viewSymbols;
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

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "image")
	public Set<ViewSymbol> getViewSymbols() {
		return this.viewSymbols;
	}

	public void setViewSymbols(Set<ViewSymbol> viewSymbols) {
		this.viewSymbols = viewSymbols;
	}

}
