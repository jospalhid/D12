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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CrownService;
import services.ProjectService;
import services.RewardService;
import controllers.AbstractController;
import domain.Crown;
import domain.Project;
import domain.Reward;

@Controller
@RequestMapping("/reward/crown")
public class RewardCrownController extends AbstractController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CrownService crownService;
	@Autowired
	private RewardService rewardService;
	
	// Constructors -----------------------------------------------------------

	public RewardCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		Reward reward = this.rewardService.create(project);
		
		result = new ModelAndView("reward/create");
		result.addObject("reward", reward);

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(Reward reward, BindingResult binding) {
		ModelAndView result;
		
		Reward res = this.rewardService.reconstruct(reward, binding);
		if(!binding.hasErrors()){
			try{
				this.rewardService.save(res);
				
				int projectId = res.getProject().getId();
				Project project = this.projectService.findOne(projectId);
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
				result = new ModelAndView("reward/create");
				result.addObject("reward", reward);
				result.addObject("message", "reward.commint.error");
			}
		}else{
			result = new ModelAndView("reward/create");
			result.addObject("reward", reward);
		}
		
		return result;
	}
	
	@RequestMapping(value="/delete",method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int rewardId) {
		ModelAndView result;
		Reward reward = this.rewardService.findOne(rewardId);
//		try{
			int projectId = reward.getProject().getId();
			Project project = this.projectService.findOne(projectId);
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
			
			if(reward.getCrowns().isEmpty()){
				this.rewardService.delete(reward);
			}else{
				result.addObject("message", "project.backer.error");
			}
//		}catch(Throwable oops){
//			int projectId = reward.getProject().getId();
//			Project project = this.projectService.findOne(projectId);
//			Long days = this.projectService.getDaysToGo(projectId);
//			Integer brackers = this.projectService.getBackers(projectId);
//			Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
//			
//			result = new ModelAndView("project/display");
//			result.addObject("project", project);
//			Double currentGoal =  this.projectService.getCurrentGoal(projectId);
//			if(currentGoal==null){
//				currentGoal=0.0;
//			}
//			result.addObject("currentGoal", currentGoal);
//			result.addObject("days", days);
//			result.addObject("brackers", brackers);
//			result.addObject("crown", crown);
//			
//			result.addObject("message", "project.commit.error");
//		}

		return result;
	}
	
}
