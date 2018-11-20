package dictionary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "meaning", catalog = "dictionary")
public class Meaning implements java.io.Serializable {

	private Integer id;
	private Clarifier clarifier;
	private String word;

	public Meaning() {
	}

	public Meaning(Clarifier clarifier, String word) {
		this.clarifier = clarifier;
		this.word = word;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clarifier_id", nullable = false)
	public Clarifier getClarifier() {
		return this.clarifier;
	}

	public void setClarifier(Clarifier clarifier) {
		this.clarifier = clarifier;
	}

	@Column(name = "word", nullable = false, length = 45)
	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
