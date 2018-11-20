package dictionary.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "clarifier", catalog = "dictionary")
public class Clarifier implements java.io.Serializable {

	private Integer id;
	@NotNull(message = "名前を入力してください")
	@Pattern(regexp = "^[a-zA-Z]*$", message = "半角英字以外の入力はできません")
	private String typeName;
	private Set<Meaning> meanings = new HashSet<Meaning>(0);
	private Set<ViewSymbolHasClarifier> viewSymbolHasClarifiers = new HashSet<ViewSymbolHasClarifier>(
			0);

	public Clarifier() {
	}

	public Clarifier(String typeName) {
		this.typeName = typeName;
	}
	public Clarifier(String typeName, Set<Meaning> meanings,
			Set<ViewSymbolHasClarifier> viewSymbolHasClarifiers) {
		this.typeName = typeName;
		this.meanings = meanings;
		this.viewSymbolHasClarifiers = viewSymbolHasClarifiers;
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

	@Column(name = "type_name", nullable = false, length = 45)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "clarifier")
	public Set<Meaning> getMeanings() {
		return this.meanings;
	}

	public void setMeanings(Set<Meaning> meanings) {
		this.meanings = meanings;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "clarifier")
	public Set<ViewSymbolHasClarifier> getViewSymbolHasClarifiers() {
		return this.viewSymbolHasClarifiers;
	}

	public void setViewSymbolHasClarifiers(
			Set<ViewSymbolHasClarifier> viewSymbolHasClarifiers) {
		this.viewSymbolHasClarifiers = viewSymbolHasClarifiers;
	}

}
