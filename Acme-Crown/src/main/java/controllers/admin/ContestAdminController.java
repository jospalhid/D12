/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContestService;
import controllers.AbstractController;
import domain.Contest;
import domain.Project;

@Controller
@RequestMapping("/contest/admin")
public class ContestAdminController extends AbstractController {
	
	@Autowired
	private ContestService contestService;
	
	// Constructors -----------------------------------------------------------

	public ContestAdminController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	@RequestMapping(value="/list")
	public ModelAndView list() {
		ModelAndView result;
		
		Collection<Contest> contests = this.contestService.findNotWinner();
		
		result = new ModelAndView("contest/winner");
		result.addObject("contests", contests);
		result.addObject("requestURI", "/contest/admin/list.do");
		result.addObject("up", true);
		
		return result;
	}
	
	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		
		Contest contest = this.contestService.create();
		
		result = new ModelAndView("contest/create");
		result.addObject("contest", contest);

		return result;
	}
	
	@RequestMapping(value="/edit",method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int contestId) {
		ModelAndView result;
		
		Contest contest = this.contestService.findOne(contestId);
		
		result = new ModelAndView("contest/edit");
		result.addObject("contest", contest);
		result.addObject("borrar", contest.getProjects().isEmpty());
		
		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(Contest contest, BindingResult binding) {
		ModelAndView result;
		
		this.contestService.validate(contest, binding);
		if(!binding.hasErrors()){
			try{
				this.contestService.reconstructAndSave(contest);
				
				Collection<Contest> contests = this.contestService.findAvailableContest();

				result = new ModelAndView("contest/available");
				result.addObject("contests", contests);
				result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
				result.addObject("requestURI", "contest/available.do");
				
			}catch(Throwable oops){
				if(contest.getId()==0){
					result = new ModelAndView("contest/create");
				}else{
					result = new ModelAndView("contest/edit");
				}
				result.addObject("contest", contest);
				result.addObject("message", "contest.commint.error");
			}
		}else{
			if(contest.getId()==0){
				result = new ModelAndView("contest/create");
			}else{
				result = new ModelAndView("contest/edit");
			}
			result.addObject("contest", contest);
		}
		
		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="delete")
	public ModelAndView edit(Contest contest) {
		ModelAndView result;
		
		try{
			Contest res = this.contestService.findOne(contest.getId());
			this.contestService.delete(res);
			
			Collection<Contest> contests = this.contestService.findAvailableContest();

			result = new ModelAndView("contest/available");
			result.addObject("contests", contests);
			result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
			result.addObject("requestURI", "contest/available.do");
			
		}catch(Throwable oops){

			result = new ModelAndView("contest/edit");
			result.addObject("contest", contest);
			result.addObject("message", "contest.commint.error");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/winner", method = RequestMethod.GET)
	public ModelAndView winner(@RequestParam final int contestId) {
		ModelAndView result;

		
		
		result = new ModelAndView("contest/winner");
	
		result.addObject("requestURI", "/contest/admin/list.do");
		result.addObject("up", true);
		
		try{
			List<Project> projects = new ArrayList<Project>();
			projects.addAll(this.contestService.getWinner(contestId));
			if(!projects.isEmpty()){
				this.contestService.setWinner(contestId, projects.get(0));
			}else{
				this.contestService.setWinner(contestId);
			}
		}catch(Throwable oops){
			result.addObject("message", "contest.commit.error");
		}
		
		Collection<Contest> contests = this.contestService.findNotWinner();
		result.addObject("contests", contests);

		return result;
	}
}
