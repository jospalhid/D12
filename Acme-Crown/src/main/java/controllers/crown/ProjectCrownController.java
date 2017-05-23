/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.crown;

import java.util.Calendar;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CategoryService;
import services.ConfigService;
import services.CreditCardService;
import services.CrownService;
import services.ProjectService;
import services.RewardService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Crown;
import domain.Picture;
import domain.Project;
import domain.Reward;
import forms.ProjectForm;

@Controller
@RequestMapping("/project/crown")
public class ProjectCrownController extends AbstractController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CrownService crownService;
	@Autowired
	private RewardService rewardService;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private ConfigService configService;
	
	// Constructors -----------------------------------------------------------

	public ProjectCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		int id=LoginService.getPrincipal().getId();
		Collection<Project> projects = this.projectService.findMyProjects(id);

		result = new ModelAndView("project/mine");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
		result.addObject("requestURI", "project/crown/list.do");

		return result;
	}
	
	@RequestMapping("/contributions")
	public ModelAndView contributions() {
		ModelAndView result;
		int id=LoginService.getPrincipal().getId();
		Collection<Project> projects = this.projectService.findMyContributions(id);

		result = new ModelAndView("project/contributions");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
		result.addObject("requestURI", "project/crown/list.do");

		return result;
	}

	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		
		ProjectForm project = new ProjectForm();

		result = new ModelAndView("project/create");
		result.addObject("projectForm", project);
		result.addObject("categories", this.categoryService.findAll());

		return result;
	}
	
	@RequestMapping(value="/edit",method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		
		ProjectForm res = new ProjectForm(projectId, project.getTitle(), project.getDescription(), project.getGoal(), this.projectService.getDaysToGo(projectId), project.getCategory());

		result = new ModelAndView("project/edit");
		result.addObject("projectForm", res);
		result.addObject("categories", this.categoryService.findAll());
		if(this.projectService.getBackers(projectId)==0){
			result.addObject("borrar", true);
		}

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(ProjectForm project, BindingResult binding) {
		ModelAndView result;
		
		try{
			ProjectForm res= this.projectService.validate(project, binding);
			if(!binding.hasErrors()){
				try{
					Project save=this.projectService.reconstructAndSave(res);
					
					result = new ModelAndView("project/display");
					result.addObject("project", save);
					Double currentGoal =  this.projectService.getCurrentGoal(save.getId());
					if(currentGoal==null){
						currentGoal=0.0;
					}
					result.addObject("currentGoal", currentGoal);
					result.addObject("days", this.projectService.getDaysToGo(save.getId()));
					result.addObject("brackers", this.projectService.getBackers(save.getId()));
					result.addObject("crown", this.crownService.findByUserAccountId(LoginService.getPrincipal().getId()));
				}
				catch(Throwable oops){
					if(project.getId()==0){
						result = new ModelAndView("project/create");
					}else{
						result = new ModelAndView("project/edit");
					}
					result.addObject("projectForm", project);
					result.addObject("categories", this.categoryService.findAll());
					if(project.getId()!=0 && !this.projectService.isValidTtl(project.getId(), project.getTtl())){
						result.addObject("message", "project.days.error");
					}else{
						result.addObject("message", "project.commit.error");
					}
				}
			}else{
				if(project.getId()==0){
					result = new ModelAndView("project/create");
				}else{
					result = new ModelAndView("project/edit");
				}
				result.addObject("projectForm", project);
				result.addObject("categories", this.categoryService.findAll());
			}
		}catch(Throwable opps){
			if(project.getId()==0){
				result = new ModelAndView("project/create");
			}else{
				result = new ModelAndView("project/edit");
			}
			result.addObject("project", project);
			result.addObject("categories", this.categoryService.findAll());
			try{
				if(project.getId()!=0 && !this.projectService.isValidTtl(project.getId(), project.getTtl())){
					result.addObject("message", "project.days.error");
				}else{
					result.addObject("message", "project.commit.error");
				}
			}catch(Throwable opss){
				result.addObject("message", "project.commit.error");
			}
		}

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="delete")
	public ModelAndView delete(ProjectForm project) {
		ModelAndView result;
		
		try{
			if(this.projectService.getBackers(project.getId())==0){
				this.projectService.reconstructAndDelete(project);
				
				Collection<Project> projects = this.projectService.findMyProjects(LoginService.getPrincipal().getId());

				result = new ModelAndView("project/available");
				result.addObject("projects", projects);
				result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
				result.addObject("requestURI", "project/crown/list.do");
				result.addObject("message", "project.delete.success");
			}else{
				result = new ModelAndView("project/edit");
				result.addObject("projectForm", project);
				result.addObject("categories", this.categoryService.findAll());
				result.addObject("message", "project.backer.error");
			}
		}catch(Throwable opps){
			result = new ModelAndView("project/edit");
			result.addObject("projectForm", project);
			result.addObject("categories", this.categoryService.findAll());
			result.addObject("message", "project.commit.error");
		}

		return result;
	}
	
	@RequestMapping(value="/reward",method = RequestMethod.GET)
	public ModelAndView reward(@RequestParam int rewardId) {
		ModelAndView result;
		
		Reward reward = this.rewardService.findOne(rewardId);
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		CreditCard card = crown.getCreditCard();
		
		if(card!=null){
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH)+1;
			if((card.getExpirationYear()+2000)<year || (card.getExpirationYear()+2000)==year && card.getExpirationMonth()<=month){
				result = new ModelAndView("creditCard/edit");
				result.addObject("creditCard", card);
				result.addObject("message", "reward.invalid.creditCard");
			}else{
				result = new ModelAndView("project/reward");
				result.addObject("reward", reward);
				result.addObject("card", card);
				result.addObject("number", card.getNumber().substring(12));
			}
		}else{
			result = new ModelAndView("creditCard/edit");
			result.addObject("creditCard", this.creditCardService.create(crown));
			result.addObject("message", "reward.invalid.creditCard");
		}
		
		return result;
	}
	
	@RequestMapping(value="/reward",method = RequestMethod.POST, params="save")
	public ModelAndView reward(Reward reward) {
		ModelAndView result;
		
		try{
			this.rewardService.newCrown(reward.getId());
			int projectId = this.rewardService.findOne(reward.getId()).getProject().getId();
			Project project = this.projectService.findOne(projectId);
			Long days = this.projectService.getDaysToGo(projectId);
			Integer brackers = this.projectService.getBackers(projectId);
			Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			
			result = new ModelAndView("project/display");
			result.addObject("project", project);
			Double currentGoal=this.projectService.getCurrentGoal(projectId);
			if(currentGoal==null){
				currentGoal=0.0;
			}
			result.addObject("currentGoal", currentGoal);
			result.addObject("days", days);
			result.addObject("brackers", brackers);
			result.addObject("crown", crown);
			result.addObject("patron", "CROWNED!");
		}catch(Throwable oops){
			Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			CreditCard card = crown.getCreditCard();
			result = new ModelAndView("project/reward");
			result.addObject("reward", this.rewardService.findOne(reward.getId()));
			result.addObject("card", card);
			result.addObject("number", card.getNumber().substring(12));
			result.addObject("message", "project.commit.error");
			
		}
		
		return result;
	}
	
	@RequestMapping(value="/picture",method = RequestMethod.GET)
	public ModelAndView picture(@RequestParam int projectId) {
		ModelAndView result;
		
		Picture picture = new Picture();
		
		result = new ModelAndView("project/picture");
		result.addObject("picture", picture);
		result.addObject("projectId", projectId);

		return result;
	}
	
	@RequestMapping(value="/picture", method = RequestMethod.POST, params="save")
	public ModelAndView picture(int projectId, @Valid Picture picture, BindingResult binding) {
		ModelAndView result;
		
		if(!binding.hasErrors()){
			try{
				Project project = this.projectService.findOne(projectId);
				project.getPictures().add(picture);
				this.projectService.save(project);
				
				
				Long days = this.projectService.getDaysToGo(projectId);
				Integer brackers = this.projectService.getBackers(projectId);
				Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
				
				result = new ModelAndView("project/display");
				result.addObject("project", project);
				Double currentGoal =  this.projectService.getCurrentGoal(projectId);
				if(currentGoal==null){
					currentGoal=0.0;
				}
				result.addObject("currentGoal", currentGoal);
				result.addObject("days", days);
				result.addObject("brackers", brackers);
				result.addObject("crown", crown);
			}catch(Throwable oops){
				result = new ModelAndView("project/picture");
				result.addObject("picture", picture);
				result.addObject("projectId", projectId);
				result.addObject("message", "project.commint.error");
			}
		}else{
			result = new ModelAndView("project/picture");
			result.addObject("picture", picture);
			result.addObject("projectId", projectId);
		}
		
		return result;
	}
	
	
	@RequestMapping(value="/promote",method = RequestMethod.GET)
	public ModelAndView promote(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		project.setPromoted(true);
		Project res = this.projectService.save(project);
		
		Long days = this.projectService.getDaysToGo(projectId);
		Integer brackers = this.projectService.getBackers(projectId);
		Crown crown1 = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		
		crown1.setAmount(crown1.getAmount()+this.configService.find().getFee());
		Crown crown = this.crownService.save(crown1);
		
		result = new ModelAndView("project/display");
		result.addObject("project", res);
		Double currentGoal =  this.projectService.getCurrentGoal(projectId);
		if(currentGoal==null){
			currentGoal=0.0;
		}
		result.addObject("currentGoal", currentGoal);
		result.addObject("days", days);
		result.addObject("brackers", brackers);
		result.addObject("crown", crown);
		result.addObject("promotedSMS", "proyect.promoted.success");
		result.addObject("promoted", true);
		

		return result;
	}
	
	

}
