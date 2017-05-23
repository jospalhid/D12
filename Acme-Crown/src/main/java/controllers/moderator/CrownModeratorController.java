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
import services.CrownService;
import services.ModeratorService;
import controllers.AbstractController;
import domain.Crown;

@Controller
@RequestMapping("/crown/moderator")
public class CrownModeratorController extends AbstractController {
	
	@Autowired
	private CrownService crownService;
	@Autowired
	private ModeratorService moderatorService;
	
	// Constructors -----------------------------------------------------------

	public CrownModeratorController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		Collection<Crown> crowns = this.crownService.findAll();
		
		result = new ModelAndView("crown/list");
		result.addObject("crowns", crowns);
		result.addObject("requestURI", "/crown/moderator/list.do");
		int level =  this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId()).getLevel();
		result.addObject("level",level);

		return result;
	}
	
	@RequestMapping(value="/ban",method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int crownId) {
		ModelAndView result;
		
		Collection<Crown> crowns = this.crownService.findAll();
		
		result = new ModelAndView("crown/list");
		result.addObject("crowns", crowns);
		result.addObject("requestURI", "/crown/moderator/list.do");
		int level =  this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId()).getLevel();
		result.addObject("level",level);
		
		try{
			this.crownService.ban(crownId);
		}catch(Throwable oops){
			result.addObject("message", "crow.commit.error");
		}
		
		return result;
	}
	
	@RequestMapping(value="/unban",method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam int crownId) {
		ModelAndView result;
		
		Collection<Crown> crowns = this.crownService.findAll();
		
		result = new ModelAndView("crown/list");
		result.addObject("crowns", crowns);
		result.addObject("requestURI", "/crown/moderator/list.do");
		int level =  this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId()).getLevel();
		result.addObject("level",level);
		
		try{
			this.crownService.unban(crownId);
		}catch(Throwable oops){
			result.addObject("message", "crow.commit.error");
		}

		return result;
	}
	
}
