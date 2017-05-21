package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Crown extends Actor{

	//----------------------Attributes-------------------------
	private boolean banned;
	private double amount;

	public boolean isBanned() {
		return banned;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
	@Min(0)
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}




	//---------------------Relationships--------------------------
	private CreditCard creditCard;
	private Collection<Project> projects;
	private Collection<Project> favs;
	private Collection<Reward> rewards;

	@Valid
	@OneToOne(mappedBy="crown", optional=true)
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy="crown")
	public Collection<Project> getProjects() {
		return projects;
	}
	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}
	
	@Valid
	@NotNull
	@ManyToMany()
	public Collection<Project> getFavs() {
		return favs;
	}
	public void setFavs(Collection<Project> favs) {
		this.favs = favs;
	}
	
	@Valid
	@NotNull
	@ManyToMany(
			cascade={javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE},
			mappedBy="crowns",
			targetEntity=Reward.class
			)
	public Collection<Reward> getRewards() {
		return rewards;
	}
	public void setRewards(Collection<Reward> rewards) {
		this.rewards = rewards;
	}
	
	
	
	
}
