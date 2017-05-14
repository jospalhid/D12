package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;


@Entity
@Access(AccessType.PROPERTY)
public class Reward extends DomainEntity{

	//----------------------Attributes-------------------------
	private String title;
	private String description;
	private double cost;
	
	@NotBlank
	@SafeHtml
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	@SafeHtml
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Min(0)
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}

	//---------------------Relationships--------------------------
	private Project project;
	private Collection<Crown> crowns;
		@Valid
	@NotNull
	@ManyToOne(optional=false)
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	@Valid
	@NotNull
	@ManyToMany(
			targetEntity=Crown.class, 
			cascade={javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE}
			)
	@JoinTable(
			name="reward_crown",
			joinColumns=@JoinColumn(name="reward_id"),
			inverseJoinColumns=@JoinColumn(name="crown_id")
			)
	public Collection<Crown> getCrowns() {
		return crowns;
	}
	public void setCrowns(Collection<Crown> crowns) {
		this.crowns = crowns;
	}
	
}