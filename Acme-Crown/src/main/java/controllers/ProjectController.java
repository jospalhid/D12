/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProjectService;
import domain.Project;

@Controller
@RequestMapping("/project")
public class ProjectController extends AbstractController {
	
	@Autowired
	private ProjectService projectService;

	// Constructors -----------------------------------------------------------

	public ProjectController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping("/available")
	public ModelAndView available() {
		ModelAndView result;
		
		Collection<Project> projects = this.projectService.findAvailableProjects();

		result = new ModelAndView("project/available");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
		result.addObject("requestURI", "project/available.do");

		return result;
	}
	
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam int projectId) {
		ModelAndView result;
		
		Project project = this.projectService.findOne(projectId);
		Long days = this.projectService.getDaysToGo(projectId);
		Integer brackers = this.projectService.getBackers(projectId);
		
		result = new ModelAndView("project/display");
		result.addObject("project", project);
		result.addObject("currentGoal", this.projectService.getCurrentGoal(projectId));
		result.addObject("days", days);
		result.addObject("brackers", brackers);

		return result;
	}

}
