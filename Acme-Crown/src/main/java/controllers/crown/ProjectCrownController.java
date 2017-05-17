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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CategoryService;
import services.CrownService;
import services.ProjectService;
import services.RewardService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Crown;
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
	
	// Constructors -----------------------------------------------------------

	public ProjectCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

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

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(ProjectForm project, BindingResult binding) {
		ModelAndView result;
		
//		try{
			Project res= this.projectService.reconstruct(project, binding);
			if(!binding.hasErrors()){
				try{
					Project save=this.projectService.save(res);
					
					//TODO Redireccionar al display
					result = new ModelAndView("project/display");
					result.addObject("project", save);
					result.addObject("currentGoal", this.projectService.getCurrentGoal(save.getId()));
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
					result.addObject("message", "project.commit.error");
				}
			}else{
				if(project.getId()==0){
					result = new ModelAndView("project/create");
				}else{
					result = new ModelAndView("project/edit");
				}
				result.addObject("projectForm", project);
				result.addObject("categories", this.categoryService.findAll());
//				result.addObject("message", "project.commit.incomplete");
//				result.addObject("errors", this.projectService.getErrores(binding));
			}
//		}catch(Throwable opps){
//			if(project.getId()==0){
//				result = new ModelAndView("project/create");
//			}else{
//				result = new ModelAndView("project/edit");
//			}
//			result.addObject("project", project);
//			result.addObject("categories", this.categoryService.findAll());
////			result.addObject("errors", this.projectService.getErrores(binding));
//		}

		return result;
	}
	
	@RequestMapping(value="/reward",method = RequestMethod.GET)
	public ModelAndView reward(@RequestParam int rewardId) {
		ModelAndView result;
		
		Reward reward = this.rewardService.findOne(rewardId);
		CreditCard card = crownService.findByUserAccountId(LoginService.getPrincipal().getId()).getCreditCard();
		
		if(card!=null){
			result = new ModelAndView("project/reward");
			result.addObject("reward", reward);
			result.addObject("card", card);
			result.addObject("number", card.getNumber().substring(12));
		}else{
			Collection<Project> projects = this.projectService.findAvailableProjects();

			result = new ModelAndView("project/available");
			result.addObject("projects", projects);
			result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
			result.addObject("requestURI", "project/available.do");
			result.addObject("message", "project.card.error");
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
			result.addObject("currentGoal", this.projectService.getCurrentGoal(projectId));
			result.addObject("days", days);
			result.addObject("brackers", brackers);
			result.addObject("crown", crown);
			result.addObject("patron", "CROWNED!");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:reward.do");
		}
		
		return result;
	}
	
	
	

}
