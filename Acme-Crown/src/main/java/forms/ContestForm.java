package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import domain.Project;

@Access(AccessType.PROPERTY)
public class ContestForm{

	//----------------------Attributes-------------------------
	private int contestId;
	private Project project;
	
	public ContestForm(int contestId) {
		this.contestId = contestId;
	}
	
	@Min(0)
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	
	@Valid
	@NotNull
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	
	
	
}
