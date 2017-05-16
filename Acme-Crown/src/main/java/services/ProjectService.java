package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

@Service
@Transactional
public class ProjectService {

	//Managed repository
	@Autowired
	private ProjectRepository	projectRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

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
		a.setAuthority(Authority.CUSTOMER);
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
	
	public Collection<Project> findAvailableProjects(){
		return this.projectRepository.findAvailableProjects();
	}

}