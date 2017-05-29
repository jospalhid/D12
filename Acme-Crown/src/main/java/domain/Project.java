
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "banned, ttl")})
public class Project extends DomainEntity {

	//----------------------Attributes-------------------------
	private String				title;
	private String				description;
	private int					goal;
	private Date				ttl;
	private Date				moment;
	private Collection<Picture>	pictures;
	private boolean				banned;
	private boolean				promoted;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotNull
	@SafeHtml
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@Min(0)
	public int getGoal() {
		return this.goal;
	}
	public void setGoal(final int goal) {
		this.goal = goal;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getTtl() {
		return this.ttl;
	}
	public void setTtl(final Date ttl) {
		this.ttl = ttl;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@ElementCollection
	@Valid
	@NotNull
	public Collection<Picture> getPictures() {
		return this.pictures;
	}
	public void setPictures(final Collection<Picture> pictures) {
		this.pictures = pictures;
	}

	public boolean isBanned() {
		return this.banned;
	}
	public void setBanned(final boolean banned) {
		this.banned = banned;
	}

	public boolean isPromoted() {
		return this.promoted;
	}
	public void setPromoted(final boolean promoted) {
		this.promoted = promoted;
	}


	//---------------------Relationships--------------------------
	private Crown					crown;
	private Category				category;
	private Collection<Reward>		rewards;
	private Collection<ExtraReward>	extraRewards;
	private Collection<Contest>		contests;
	private Collection<Comment>		comments;


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
	public Category getCategory() {
		return this.category;
	}
	public void setCategory(final Category category) {
		this.category = category;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	public Collection<Reward> getRewards() {
		return this.rewards;
	}
	public void setRewards(final Collection<Reward> rewards) {
		this.rewards = rewards;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	public Collection<ExtraReward> getExtraRewards() {
		return this.extraRewards;
	}
	public void setExtraRewards(final Collection<ExtraReward> extraRewards) {
		this.extraRewards = extraRewards;
	}

	@Valid
	@NotNull
	@ManyToMany(cascade = {
		javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE
	}, mappedBy = "projects", targetEntity = Contest.class)
	public Collection<Contest> getContests() {
		return this.contests;
	}
	public void setContests(final Collection<Contest> contests) {
		this.contests = contests;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "project")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

}
