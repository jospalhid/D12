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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CommentService;
import services.CrownService;
import services.ProjectService;
import controllers.AbstractController;
import domain.Comment;
import domain.Crown;
import domain.Project;

@Controller
@RequestMapping("/comment/moderator")
public class CommentModeratorController extends AbstractController {
	
	@Autowired
	private CommentService commentService;
	@Autowired
	private CrownService crownService;
	@Autowired
	private ProjectService projectService;
	
	// Constructors -----------------------------------------------------------

	public CommentModeratorController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping(value="/ban",method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int commentId) {
		ModelAndView result;
		result = new ModelAndView("project/display");
		Comment comment = this.commentService.findOne(commentId);
		int projectId = comment.getProject().getId();

		try{
			this.commentService.ban(comment);
		}catch(Throwable oops){
			result.addObject("message", "comment.commit.error");
		}
		
		final Project project = this.projectService.findOne(projectId);
		final Long days = this.projectService.getDaysToGo(projectId);
		final Integer brackers = this.projectService.getBackers(projectId);
		final Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		final Collection<Comment> comments = this.commentService.findReceivedComments(projectId);
		
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
	
}
