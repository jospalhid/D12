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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContestService;
import services.ProjectService;
import controllers.AbstractController;
import domain.Contest;
import domain.Project;
import forms.ContestForm;

@Controller
@RequestMapping("/contest/crown")
public class ContestCrownController extends AbstractController {
	
	@Autowired
	private ContestService contestService;
	@Autowired
	private ProjectService projectService;

	// Constructors -----------------------------------------------------------

	public ContestCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping("/join")
	public ModelAndView join(@RequestParam int contestId) {
		ModelAndView result;
		
		Collection<Project> projects = this.projectService.findMyContestProjects(contestId);
		
		ContestForm contestForm = new ContestForm(contestId);
		
		result = new ModelAndView("contest/join");
		result.addObject("contestForm", contestForm);
		result.addObject("projects", projects);

		return result;
	}
	
	
	@RequestMapping(value="/join", method = RequestMethod.POST, params="save")
	public ModelAndView join(ContestForm contestForm) {
		ModelAndView result;
		
		try{
			Contest contest = this.contestService.join(contestForm);
			Collection<Project> projects = contest.getProjects();
			
			result = new ModelAndView("contest/display");
			result.addObject("contest", contest);
			result.addObject("projects", projects);
			
		}catch(Throwable oops){
			Collection<Project> projects = this.projectService.findMyAvailableProjects();
			
			result = new ModelAndView("contest/join");
			result.addObject("contestForm", contestForm);
			result.addObject("projects", projects);
			result.addObject("message", "contest.commint.error");
		}
		return result;
	}
		
}
