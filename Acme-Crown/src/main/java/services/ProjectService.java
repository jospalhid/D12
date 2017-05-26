
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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
import domain.Comment;
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
	private Validator			validator;

	//Supporting services
	@Autowired
	private CrownService		crownService;
	@Autowired
	private ContestService		contestService;


	//Constructors
	public ProjectService() {
		super();
	}

	//Simple CRUD methods
	public Project create(final Crown crown, final Category category) {
		Project res;
		res = new Project();
		res.setCrown(crown);
		res.setCategory(category);
		res.setExtraRewards(new ArrayList<ExtraReward>());
		res.setRewards(new ArrayList<Reward>());
		res.setPictures(new ArrayList<Picture>());
		res.setContests(new ArrayList<Contest>());
		res.setComments(new ArrayList<Comment>());
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

		final Integer days = this.getDaysToLive(project);
		Assert.isTrue(days <= 90 && days > 0, "The ttl must be 90 or less");

		//		project.setMoment(Calendar.getInstance().getTime());

		final Project res = this.projectRepository.save(project);
		return res;
	}

	public Project saveBan(final Project project) {
		Assert.notNull(project, "The project to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.MODERATOR);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a moderator to ban/unban a project.");

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
		Assert.isTrue(this.getBackers(project.getId()) == 0, "The project cannot be delete with backers");
		
		Assert.isTrue(project.getComments().isEmpty(), "Cannot be delete with comments");

		this.projectRepository.delete(project);
	}

	//Utilites methods
	public Double getCurrentGoal(final int projectId) {
		return this.projectRepository.getCurrentGoal(projectId);
	}

	public Integer getBackers(final int projectId) {
		return this.projectRepository.getBackers(projectId);
	}

	public Collection<Project> findAvailableProjects() {
		return this.projectRepository.findAvailableProjects();
	}

	public Collection<Project> findMyAvailableProjects() {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown for this action.");
		return this.projectRepository.findMyAvailableProjects(LoginService.getPrincipal().getId());
	}

	public Collection<Project> findMyContributions(final int id) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		final Authority b = new Authority();
		b.setAuthority(Authority.MODERATOR);
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(b), "You must to be a crown or a moderator for this action.");
		return this.projectRepository.findMyContributions(id);
	}

	public Collection<Project> findMyContestProjects(final int contestId) {
		final List<Project> projects = new ArrayList<Project>();
		projects.addAll(this.findMyAvailableProjects());
		final Contest contest = this.contestService.findOne(contestId);
		projects.removeAll(contest.getProjects());
		return projects;
	}

	public boolean canJoin(final int contestId) {
		boolean res = false;
		final List<Project> projects = new ArrayList<Project>();
		projects.addAll(this.findMyContestProjects(contestId));
		if (projects.size() > 0)
			res = true;
		return res;
	}

	public Collection<Project> findMyProjects(final int id) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		final Authority b = new Authority();
		b.setAuthority(Authority.MODERATOR);
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(b), "You must to be a crown or a moderator for this action.");

		return this.projectRepository.findMyProjects(id);
	}

	public Collection<Project> findMyFavs() {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown for this action.");

		return this.projectRepository.findMyFavs(ua.getId());
	}
	
	public Collection<Project> findProjectCategory(int categoryId){
		return this.projectRepository.findProjectCategory(categoryId);
		
	}
	public Long getDaysToGo(final int projectId) {
		final Project project = this.findOne(projectId);
		final Long current = Calendar.getInstance().getTimeInMillis();
		final Long moment = project.getMoment().getTime();
		final Long days = (current - moment) / 86400000;
		final Long finish = project.getTtl().getTime();
		final Long ttl = (finish - moment) / 86400000;
		return ttl - days;
	}

	public Long getDaysToGo(final Project project) {
		final Long current = Calendar.getInstance().getTimeInMillis();
		final Long moment = project.getMoment().getTime();
		final Long days = (current - moment) / 86400000;
		final Long finish = project.getTtl().getTime();
		final Long ttl = (finish - moment) / 86400000;
		return ttl - days;
	}

	public Integer getDaysToLive(final int projectId) {
		final Project project = this.findOne(projectId);
		final Long moment = project.getMoment().getTime();
		final Long finish = project.getTtl().getTime();
		final Integer ttl = (int) ((finish - moment) / 86400000);
		return ttl;
	}
	public Integer getDaysToLive(final Project project) {
		final Long moment = project.getMoment().getTime();
		final Long finish = project.getTtl().getTime();
		final Integer ttl = (int) ((finish - moment) / 86400000);
		return ttl;
	}
	public boolean isValidTtl(final int projectId, final long ttl) {
		final Project res = this.findOne(projectId);
		final Integer oldTtl = this.getDaysToLive(res.getId());
		final Long days = this.getDaysToGo(res.getId());
		final Long realTtl = oldTtl - days + ttl;
		return realTtl < 0 && realTtl > 90;
	}

	public Project reconstructAndSave(final ProjectForm project) {
		Assert.notNull(project, "The project to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to save a project.");

		Project res;
		if (project.getId() == 0) {
			final Crown crown = this.crownService.findByUserAccountId(ua.getId());
			res = this.create(crown, project.getCategory());
		} else {
			res = this.findOne(project.getId());
			Assert.isTrue(res.getCrown().getUserAccount().equals(ua), "You are not the owner of this project");
		}

		res.setDescription(project.getDescription());
		res.setTitle(project.getTitle());
		res.setGoal(project.getGoal());

		final Calendar ttl = Calendar.getInstance();
		if (project.getId() == 0)
			ttl.setTimeInMillis(res.getMoment().getTime() + project.getTtl() * 86400000);
		else {
			final Integer oldTtl = this.getDaysToLive(res.getId());
			final Long days = this.getDaysToGo(res.getId());
			final Long realTtl = oldTtl - days + project.getTtl();
			ttl.setTimeInMillis(res.getMoment().getTime() + realTtl * 86400000);
		}
		res.setTtl(ttl.getTime());
		final Integer days = this.getDaysToLive(res);

		if (project.getUrl() != null && project.getUrl() != "") {
			final Picture picture = new Picture(project.getUrl(), project.getTitle());
			res.getPictures().add(picture);
		}

		Assert.isTrue(days <= 90 && days > 0, "The ttl must be 90 or less");

		final Project fin = this.projectRepository.save(res);

		return fin;
	}

	public void reconstructAndDelete(final ProjectForm project) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to delete an project.");

		Assert.notNull(project, "The project to delete cannot be null.");
		Assert.isTrue(this.projectRepository.exists(project.getId()));

		final Project res = this.findOne(project.getId());

		Assert.isTrue(res.getCrown().getUserAccount().equals(ua), "You are not the owner of this project");
		Assert.isTrue(this.getBackers(project.getId()) == 0, "The project cannot be delete with backers");

		this.projectRepository.delete(res);

	}

	public ProjectForm validate(final ProjectForm project, final BindingResult binding) {
		this.validator.validate(project, binding);
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
