package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Access(AccessType.PROPERTY)
public class Project extends DomainEntity{

	//----------------------Attributes-------------------------
	private String title;
	private String description;
	private Date moment;
	private int goal;
	private int ttl;
	private Collection<Picture> pictures;
	private boolean banned;
	
	@NotBlank
	@SafeHtml
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotNull
	@SafeHtml
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy hh:mm")
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	
	@Min(0)
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	
	@Range(min=1, max=90)
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	@ElementCollection
	@Valid
	@NotNull
	public Collection<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(Collection<Picture> pictures) {
		this.pictures = pictures;
	}
	
	public boolean isBanned() {
		return banned;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}




	//---------------------Relationships--------------------------
	private Crown crown;
	private Category category;
	private Collection<Reward> rewards;
	private Collection<ExtraReward> extraRewards;

	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Crown getCrown() {
		return crown;
	}
	public void setCrown(Crown crown) {
		this.crown = crown;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy="project", cascade = CascadeType.ALL)
	public Collection<Reward> getRewards() {
		return rewards;
	}
	public void setRewards(Collection<Reward> rewards) {
		this.rewards = rewards;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy="project", cascade = CascadeType.ALL)
	public Collection<ExtraReward> getExtraRewards() {
		return extraRewards;
	}
	public void setExtraRewards(Collection<ExtraReward> extraRewards) {
		this.extraRewards = extraRewards;
	}
	
}
