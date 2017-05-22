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

import services.ContestService;
import domain.Contest;
import domain.Project;

@Controller
@RequestMapping("/contest")
public class ContestController extends AbstractController {
	
	@Autowired
	private ContestService contestService;

	// Constructors -----------------------------------------------------------

	public ContestController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping("/available")
	public ModelAndView available() {
		ModelAndView result;
		
		Collection<Contest> contests = this.contestService.findAvailableContest();

		result = new ModelAndView("contest/available");
		result.addObject("contests", contests);
		result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
		result.addObject("requestURI", "contest/available.do");

		return result;
	}
	
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam int contestId) {
		ModelAndView result;
		
		Contest contest = this.contestService.findOne(contestId);
		Collection<Project> projects = contest.getProjects();
		
		result = new ModelAndView("contest/display");
		result.addObject("contest", contest);
		result.addObject("projects", projects);

		return result;
	}

}
