
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	//------------------------------Attributes----------------------------
	private String	title;
	private String	text;
	private int		stars;
	private Date	moment;
	private boolean banned;

	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	public String getText() {
		return this.text;
	}
	public void setText(final String text) {
		this.text = text;
	}

	@Range(min = 1, max = 5)
	public int getStars() {
		return this.stars;
	}
	public void setStars(final int stars) {
		this.stars = stars;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}
	
	public boolean isBanned() {
		return banned;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	//-----------------------------Relationships-------------------------
	private Crown	crown;
	private Project	project;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Crown getCrown() {
		return this.crown;
	}

	public void setCrown(final Crown crown) {
		this.crown = crown;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Project getProject() {
		return this.project;
	}

	public void setProject(final Project project) {
		this.project = project;
	}

}
