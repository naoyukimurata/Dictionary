package dictionary.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "view_symbol", catalog = "dictionary")
public class ViewSymbol implements java.io.Serializable {

	private Integer id;
	private Image image;
	private MultiviewSymbol multiviewSymbol;
	private String name;
	private Set<ViewSymbolHasMeaning> viewSymbolHasMeanings = new HashSet<ViewSymbolHasMeaning>(
			0);

	public ViewSymbol() {
	}

	public ViewSymbol(Image image, MultiviewSymbol multiviewSymbol, String name) {
		this.image = image;
		this.multiviewSymbol = multiviewSymbol;
		this.name = name;
	}
	public ViewSymbol(Image image, MultiviewSymbol multiviewSymbol,
					  String name, Set<ViewSymbolHasMeaning> viewSymbolHasMeanings) {
		this.image = image;
		this.multiviewSymbol = multiviewSymbol;
		this.name = name;
		this.viewSymbolHasMeanings = viewSymbolHasMeanings;
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
	@JoinColumn(name = "image_id", nullable = false)
	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "viewSymbol")
	public Set<ViewSymbolHasMeaning> getViewSymbolHasMeanings() {
		return this.viewSymbolHasMeanings;
	}

	public void setViewSymbolHasMeanings(
			Set<ViewSymbolHasMeaning> viewSymbolHasMeanings) {
		this.viewSymbolHasMeanings = viewSymbolHasMeanings;
	}

}
