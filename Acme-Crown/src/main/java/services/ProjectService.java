package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProjectRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Category;
import domain.Contest;
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
		res.setContests(new ArrayList<Contest>());
		res.setMoment(Calendar.getInstance().getTime());
		res.setBanned(false);
		res.setPromoted(false);
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
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to save a project.");
		
		Integer days=this.getDaysToLive(project);
		Assert.isTrue(days<=90 && days>0,"The ttl must be 90 or less");
		
//		project.setMoment(Calendar.getInstance().getTime());
		
		final Project res = this.projectRepository.save(project);
		return res;
	}

	public void delete(final Project project) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to delete an project.");
		Assert.isTrue(project.getCrown().getUserAccount().equals(ua), "You are not the owner of this project");

		Assert.notNull(project, "The project to delete cannot be null.");
		Assert.isTrue(this.projectRepository.exists(project.getId()));
		Assert.isTrue(this.getBackers(project.getId())==0,"The project cannot be delete with backers");
		
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
	
	public Collection<Project> findMyProjects(){
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown for this action.");
		
		return this.projectRepository.findMyProjects(ua.getId());
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
	
	public Long getDaysToGo(Project project) {
		Long current = Calendar.getInstance().getTimeInMillis();
		Long moment = project.getMoment().getTime();
		Long days = (current-moment)/86400000;
		Long finish = project.getTtl().getTime();
		Long ttl = (finish-moment)/86400000;
		return ttl-days;
	}
	
	public Integer getDaysToLive(int projectId){
		Project project = this.findOne(projectId);
		Long moment = project.getMoment().getTime();
		Long finish = project.getTtl().getTime();
		Integer ttl = (int) ((finish-moment)/86400000);
		return ttl;
	}
	public Integer getDaysToLive(Project project){
		Long moment = project.getMoment().getTime();
		Long finish = project.getTtl().getTime();
		Integer ttl = (int) ((finish-moment)/86400000);
		return ttl;
	}
	public boolean isValidTtl(int projectId, long ttl){
		Project res = this.findOne(projectId);
		Integer oldTtl = this.getDaysToLive(res.getId());
		Long days = this.getDaysToGo(res.getId());
		Long realTtl=oldTtl-days+ttl;
		return realTtl<0 && realTtl>90;
	}

	public Project reconstructAndSave(ProjectForm project) {
		Assert.notNull(project, "The project to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to save a project.");
		
		Project res;
		if(project.getId()==0){
			Crown crown = this.crownService.findByUserAccountId(ua.getId());
			res = this.create(crown, project.getCategory());
		}else{
			res = this.findOne(project.getId());
			Assert.isTrue(res.getCrown().getUserAccount().equals(ua), "You are not the owner of this project");
		}
		
		res.setDescription(project.getDescription());
		res.setTitle(project.getTitle());
		res.setGoal(project.getGoal());
		
		Calendar ttl = Calendar.getInstance();
		if(project.getId()==0){
			ttl.setTimeInMillis(res.getMoment().getTime()+project.getTtl()*86400000);
		}else{
			Integer oldTtl = this.getDaysToLive(res.getId());
			Long days = this.getDaysToGo(res.getId());
			Long realTtl=oldTtl-days+project.getTtl();
			ttl.setTimeInMillis(res.getMoment().getTime()+realTtl*86400000);
		}
		res.setTtl(ttl.getTime());
		Integer days=this.getDaysToLive(res);
		
		if(project.getUrl()!=null && project.getUrl()!=""){
			Picture picture = new Picture(project.getUrl(), project.getTitle());
			res.getPictures().add(picture);
		}
		
		Assert.isTrue(days<=90 && days>0,"The ttl must be 90 or less");
		
		final Project fin = this.projectRepository.save(res);
		
		return fin;
	}
	
	public void reconstructAndDelete(ProjectForm project) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to delete an project.");

		Assert.notNull(project, "The project to delete cannot be null.");
		Assert.isTrue(this.projectRepository.exists(project.getId()));

		Project res = this.findOne(project.getId());
		
		Assert.isTrue(res.getCrown().getUserAccount().equals(ua), "You are not the owner of this project");
		Assert.isTrue(this.getBackers(project.getId())==0,"The project cannot be delete with backers");
		
		this.projectRepository.delete(res);
		
	}
	
	public ProjectForm validate(ProjectForm project, BindingResult binding){
		validator.validate(project, binding);
		return project;
	}

//	public Set<String> getErrores(BindingResult binding) {
//		List<ObjectError> errors = binding.getAllErrors();
//		Set<String> res = new HashSet<String>();
//		for(ObjectError wrong: errors){
//			if(wrong.toString().contains("title")){
//				res.add("Title: "+wrong.getDefaultMessage()+". ");
//			}
//			if(wrong.toString().contains("goal")){
//				res.add("Goal: "+wrong.getDefaultMessage()+". ");
//			}
//			if(wrong.toString().contains("ttl")){
//				res.add("Time to live: "+wrong.getDefaultMessage()+". ");
//			}
//			if(wrong.toString().contains("url")){
//				res.add("Picture: "+wrong.getDefaultMessage()+". ");
//			}
//		}
//		return res;
//	}

}