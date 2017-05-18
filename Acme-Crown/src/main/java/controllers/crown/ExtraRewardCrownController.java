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
import services.ExtraRewardService;
import services.ProjectService;
import controllers.AbstractController;
import domain.Crown;
import domain.ExtraReward;
import domain.Project;

@Controller
@RequestMapping("/extrareward/crown")
public class ExtraRewardCrownController extends AbstractController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CrownService crownService;
	@Autowired
	private ExtraRewardService extrarewardService;
	
	// Constructors -----------------------------------------------------------

	public ExtraRewardCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		ExtraReward reward = this.extrarewardService.create(project);
		
		result = new ModelAndView("extrareward/create");
		result.addObject("extraReward", reward);

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(ExtraReward extrareward, BindingResult binding) {
		ModelAndView result;
		
		ExtraReward res = this.extrarewardService.reconstruct(extrareward, binding);
		if(!binding.hasErrors()){
			try{
				this.extrarewardService.save(res);
				
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
				result = new ModelAndView("extrareward/create");
				result.addObject("extraReward", extrareward);
				result.addObject("message", "extrareward.commint.error");
			}
		}else{
			result = new ModelAndView("extrareward/create");
			result.addObject("extraReward", extrareward);
		}
		
		return result;
	}
	
}
