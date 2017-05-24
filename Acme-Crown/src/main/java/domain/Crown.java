
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Crown extends SuperUser {

	//----------------------Attributes-------------------------
	private boolean	banned;
	private double	amount;


	public boolean isBanned() {
		return this.banned;
	}
	public void setBanned(final boolean banned) {
		this.banned = banned;
	}

	@Min(0)
	public double getAmount() {
		return this.amount;
	}
	public void setAmount(final double amount) {
		this.amount = amount;
	}


	//---------------------Relationships--------------------------
	private Collection<Project>	projects;
	private Collection<Project>	favs;
	private Collection<Reward>	rewards;
	private Collection<Comment>	postComments;
	private Collection<Concept> concepts;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "crown")
	public Collection<Project> getProjects() {
		return this.projects;
	}
	public void setProjects(final Collection<Project> projects) {
		this.projects = projects;
	}

	@Valid
	@NotNull
	@ManyToMany()
	public Collection<Project> getFavs() {
		return this.favs;
	}
	public void setFavs(final Collection<Project> favs) {
		this.favs = favs;
	}

	@Valid
	@NotNull
	@ManyToMany(cascade = {
		javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE
	}, mappedBy = "crowns", targetEntity = Reward.class)
	public Collection<Reward> getRewards() {
		return this.rewards;
	}
	public void setRewards(final Collection<Reward> rewards) {
		this.rewards = rewards;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "crown")
	public Collection<Comment> getPostComments() {
		return this.postComments;
	}

	public void setPostComments(final Collection<Comment> postComments) {
		this.postComments = postComments;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "crown")
	public Collection<Concept> getConcepts() {
		return concepts;
	}
	public void setConcepts(Collection<Concept> concepts) {
		this.concepts = concepts;
	}
	
}
