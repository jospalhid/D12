package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;


@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity{

	//----------------------Attributes-------------------------
	private String name;
	private String description;
	
	@NotBlank
	@SafeHtml
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotBlank
	@SafeHtml
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	//---------------------Relationships--------------------------
	private Collection<Project> projects;

	@Valid
	@NotNull
	@OneToMany(mappedBy="category")
	public Collection<Project> getProjects() {
		return projects;
	}
	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}
	
	
}
