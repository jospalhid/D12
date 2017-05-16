package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import repositories.ProjectRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Category;
import domain.Crown;
import domain.ExtraReward;
import domain.Picture;
import domain.Project;
import domain.Reward;
import forms.ProjectForm;

@Service
@Transactional
public class ProjectService {

	//Managed repository
	@Autowired
	private ProjectRepository	projectRepository;


	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services
	@Autowired
	private CrownService crownService;

	//Constructors
	public ProjectService() {
		super();
	}

	//Simple CRUD methods
	public Project create(Crown crown, Category category) {
		Project res;
		res = new Project();
		res.setCrown(crown);
		res.setCategory(category);
		res.setExtraRewards(new ArrayList<ExtraReward>());
		res.setRewards(new ArrayList<Reward>());
		res.setPictures(new ArrayList<Picture>());
		res.setMoment(Calendar.getInstance().getTime());
		return res;
	}

	public Collection<Project> findAll() {
		final Collection<Project> res = this.projectRepository.findAll();
		return res;
	}

	public Project findOne(final int projectId) {
		final Project res = this.projectRepository.findOne(projectId);
		return res;
	}

	public Project save(final Project project) {
		Assert.notNull(project, "The project to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to create a project.");
		
		project.setMoment(Calendar.getInstance().getTime());
		
		final Project res = this.projectRepository.save(project);
		return res;
	}

	public void delete(final Project project) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a customer to delete an project.");

		Assert.notNull(project, "The project to delete cannot be null.");
		Assert.isTrue(this.projectRepository.exists(project.getId()));

		this.projectRepository.delete(project);
	}

	//Utilites methods
	public Double getCurrentGoal(int projectId){
		return this.projectRepository.getCurrentGoal(projectId);
	}
	
	public 	Integer getBackers(int projectId){
		return this.projectRepository.getBackers(projectId);
	}
	
	public Collection<Project> findAvailableProjects(){
		return this.projectRepository.findAvailableProjects();
	}

	public Long getDaysToGo(int projectId) {
		Project project = this.findOne(projectId);
		Long current = Calendar.getInstance().getTimeInMillis();
		Long moment = project.getMoment().getTime();
		Long days = (current-moment)/86400000;
		Long finish = project.getTtl().getTime();
		Long ttl = (finish-moment)/86400000;
		return ttl-days;
	}

	public Project reconstruct(ProjectForm project, BindingResult binding) {
		
		validator.validate(project, binding);
		
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		Project res;
		if(project.getId()==0){
			res = this.create(crown, project.getCategory());
		}else{
			res = this.findOne(project.getId());
		}
		
		res.setDescription(project.getDescription());
		res.setTitle(project.getTitle());
		res.setGoal(project.getGoal());
		if(project.getUrl()!=""){
			res.getPictures().add(new Picture(project.getUrl(), project.getTitle()));
		}
		
		Calendar ttl = Calendar.getInstance();
		ttl.setTimeInMillis(res.getMoment().getTime()+project.getTtl()*86400000);
		
		res.setTtl(ttl.getTime());
		
		return res;
	}

	public Set<String> getErrores(BindingResult binding) {
		List<ObjectError> errors = binding.getAllErrors();
		Set<String> res = new HashSet<String>();
		for(ObjectError wrong: errors){
			if(wrong.toString().contains("title")){
				res.add("Title: "+wrong.getDefaultMessage()+". ");
			}
			if(wrong.toString().contains("goal")){
				res.add("Goal: "+wrong.getDefaultMessage()+". ");
			}
			if(wrong.toString().contains("ttl")){
				res.add("Time to live: "+wrong.getDefaultMessage()+". ");
			}
			if(wrong.toString().contains("url")){
				res.add("Picture: "+wrong.getDefaultMessage()+". ");
			}
		}
		return res;
	}

}