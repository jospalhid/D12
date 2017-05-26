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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConceptService;
import controllers.AbstractController;
import domain.Concept;

@Controller
@RequestMapping("/concept/admin")
public class ConceptAdminController extends AbstractController {
	
	@Autowired
	private ConceptService conceptService;
	// Constructors -----------------------------------------------------------

	public ConceptAdminController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping(value="/list")
	public ModelAndView list() {
		ModelAndView result;
		
		Collection<Concept> concepts = this.conceptService.findNotValidate();
		
		result = new ModelAndView("concept/validate");
		result.addObject("concepts", concepts);
		result.addObject("requestURI", "/concept/admin/list.do");
		
		return result;
	}
	
	@RequestMapping(value="/valid", method = RequestMethod.GET)
	public ModelAndView valid(@RequestParam int conceptId) {
		ModelAndView result;
		
		try{
			this.conceptService.valid(conceptId);
			result = new ModelAndView("redirect:list.do");
		}catch(Throwable oops){
			Collection<Concept> concepts = this.conceptService.findNotValidate();
			
			result = new ModelAndView("concept/validate");
			result.addObject("concepts", concepts);
			result.addObject("requestURI", "/concept/admin/list.do");
			result.addObject("message", "concept.commit.error");
		}
		
		return result;
	}
	
	@RequestMapping(value="/unvalid", method = RequestMethod.GET)
	public ModelAndView unvalid(@RequestParam int conceptId) {
		ModelAndView result;
		
		try{
			this.conceptService.unvalid(conceptId);
			result = new ModelAndView("redirect:list.do");
		}catch(Throwable oops){
			Collection<Concept> concepts = this.conceptService.findNotValidate();
			
			result = new ModelAndView("concept/validate");
			result.addObject("concepts", concepts);
			result.addObject("requestURI", "/concept/admin/list.do");
			result.addObject("message", "concept.commit.error");
		}
		
		return result;
	}
	
}