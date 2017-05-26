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

import security.LoginService;
import services.CommentService;
import services.CrownService;
import services.ProjectService;
import domain.Comment;
import domain.Crown;
import domain.Project;

@Controller
@RequestMapping("/project")
public class ProjectController extends AbstractController {

	@Autowired
	private ProjectService	projectService;
	@Autowired
	private CrownService	crownService;
	@Autowired
	private CommentService	commentService;


	// Constructors -----------------------------------------------------------

	public ProjectController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping("/available")
	public ModelAndView available() {
		ModelAndView result;

		final Collection<Project> projects = this.projectService.findAvailableProjects();

		result = new ModelAndView("project/available");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis() / 86400000);
		result.addObject("requestURI", "project/available.do");

		return result;
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int projectId) {
		ModelAndView result;

		final Project project = this.projectService.findOne(projectId);
		final Long days = this.projectService.getDaysToGo(projectId);
		final Integer brackers = this.projectService.getBackers(projectId);
		final Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		final Collection<Comment> comments = this.commentService.findReceivedComments(projectId);

		result = new ModelAndView("project/display");
		result.addObject("project", project);
		Double currentGoal = this.projectService.getCurrentGoal(projectId);
		if (currentGoal == null)
			currentGoal = 0.0;
		result.addObject("currentGoal", currentGoal);
		result.addObject("days", days);
		result.addObject("brackers", brackers);
		result.addObject("crown", crown);
		result.addObject("comments", comments);
		result.addObject("requestURI", "/project/display.do?projectId=" + projectId);
		if (crown != null)
			if (crown.getFavs().contains(project))
				result.addObject("fav", true);
			else
				result.addObject("fav", false);

		return result;
	}
	
	@RequestMapping("/list")
	public ModelAndView listCategory(@RequestParam int categoryId) {
		ModelAndView result;

		final Collection<Project> projects = this.projectService.findProjectCategory(categoryId);

		result = new ModelAndView("project/list");
		result.addObject("projects", projects);
		result.addObject("current", Calendar.getInstance().getTimeInMillis() / 86400000);
		result.addObject("requestURI", "/project/list.do");

		return result;
	}


}
