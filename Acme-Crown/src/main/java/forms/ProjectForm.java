package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import domain.Category;

@Access(AccessType.PROPERTY)
public class ProjectForm{

	//----------------------Attributes-------------------------
	private int id;
	private String title;
	private String description;
	private int goal;
	private long ttl;
	private String url;
	private Category category;
	
	public ProjectForm(){}
	
	public ProjectForm(int id, String title, String description, int goal, long ttl, Category category) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.goal = goal;
		this.ttl = ttl;
		this.category = category;
	}



	@Min(0)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
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
	
	@Min(0)
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	
	@Range(min=1, max=90)
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	
	@URL
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Valid
	@NotNull
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	
	
	
}
