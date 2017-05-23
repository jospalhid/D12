/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.moderator;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CrownService;
import services.ModeratorService;
import services.ProjectService;
import controllers.AbstractController;
import domain.Crown;
import domain.Moderator;
import domain.Project;

@Controller
@RequestMapping("/project/moderator")
public class ProjectModeratorController extends AbstractController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CrownService crownService;
	@Autowired
	private ModeratorService moderatorService;

	// Constructors -----------------------------------------------------------

	public ProjectModeratorController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		
		Collection<Project> projects = this.projectService.findAll();

		result = new ModelAndView("project/all");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
		result.addObject("requestURI", "project/moderator/list.do");

		return result;
	}
	
	@RequestMapping("/crown")
	public ModelAndView crown() {
		ModelAndView result;
		Set<Project> projects = new HashSet<Project>();
		Moderator moderator = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
		if(moderator.getCrown()!=null){
			int crownId = moderator.getCrown().getUserAccount().getId();
			projects.addAll(this.projectService.findMyContributions(crownId));
			projects.addAll(this.projectService.findMyProjects(crownId));
		}
		
		result = new ModelAndView("project/all");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
		result.addObject("requestURI", "project/moderator/crown.do");

		return result;
	}
	
	@RequestMapping(value="/ban",method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		project.setBanned(true);
		Project res = this.projectService.saveBan(project);
		
		Long days = this.projectService.getDaysToGo(projectId);
		Integer brackers = this.projectService.getBackers(projectId);
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		
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
		result.addObject("promotedSMS", "proyect.ban.success");
		result.addObject("promoted", true);
		

		return result;
	}
	
	@RequestMapping(value="/unban",method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		project.setBanned(false);
		Project res = this.projectService.saveBan(project);
		
		Long days = this.projectService.getDaysToGo(projectId);
		Integer brackers = this.projectService.getBackers(projectId);
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		
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
		result.addObject("promotedSMS", "proyect.unban.success");
		result.addObject("promoted", true);
		

		return result;
	}
	
	

}
