package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
public class Contest extends DomainEntity{

	//----------------------Attributes-------------------------
	private String title;
	private String topic;
	private String description;
	private Date moment;
	public double award;
	
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
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
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
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	
	@Min(0)
	public double getAward() {
		return award;
	}
	public void setAward(double award) {
		this.award = award;
	}
	

	//---------------------Relationships--------------------------
	private Collection<Project> projects;
	private Project winner;

	@Valid
	@NotNull
	@ManyToMany(
			targetEntity=Project.class, 
			cascade={javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE}
			)
	@JoinTable(
			name="contest_project",
			joinColumns=@JoinColumn(name="contest_id"),
			inverseJoinColumns=@JoinColumn(name="project_id")
			)
	public Collection<Project> getProjects() {
		return projects;
	}
	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}
	
	@Valid
	@ManyToOne(optional=true)
	public Project getWinner() {
		return winner;
	}
	public void setWinner(Project winner) {
		this.winner = winner;
	}
	
	
	
}
