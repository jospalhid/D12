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

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ConceptService;
import services.CreditCardService;
import services.CrownService;
import controllers.AbstractController;
import domain.Concept;
import domain.CreditCard;
import domain.Crown;

@Controller
@RequestMapping("/concept/crown")
public class ConceptCrownController extends AbstractController {
	
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private CrownService crownService;
	@Autowired
	private CreditCardService creditCardService;

	// Constructors -----------------------------------------------------------

	public ConceptCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping(value="/list")
	public ModelAndView list() {
		ModelAndView result;
		
		Collection<Concept> concepts = this.conceptService.findMyConcept();
		
		result = new ModelAndView("concept/mine");
		result.addObject("concepts", concepts);
		result.addObject("requestURI", "/concept/crown/list.do");

		return result;
	}

	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		Concept concept = this.conceptService.create(crown);
		final CreditCard card = crown.getCreditCard();

		if (card != null) {
			final int year = Calendar.getInstance().get(Calendar.YEAR);
			final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			if ((card.getExpirationYear() + 2000) < year || (card.getExpirationYear() + 2000) == year && card.getExpirationMonth() <= month) {
				result = new ModelAndView("creditCard/edit");
				result.addObject("creditCard", card);
				result.addObject("message", "concept.invalid.creditCard");
			}
			result = new ModelAndView("concept/create");
			result.addObject("concept", concept);
		} else {
			result = new ModelAndView("creditCard/create");
			result.addObject("creditCard", this.creditCardService.create(crown));
			result.addObject("message", "concept.invalid.creditCard");
		}
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(final Concept concept, final BindingResult binding) {
		ModelAndView result;
		final Concept res = this.conceptService.reconstruct(concept, binding);

		if (!binding.hasErrors()){
			try {
				this.conceptService.save(res);
				this.crownService.auction();
				
				Collection<Concept> concepts = this.conceptService.findMyConcept();
				
				result = new ModelAndView("concept/mine");
				result.addObject("concepts", concepts);
				result.addObject("requestURI", "/concept/crown/list.do");
				
			} catch (final Throwable opps) {
				result = new ModelAndView("concept/create");
				result.addObject("concept", concept);
				result.addObject("message", "concept.commit.error");
			}
		}
		else {
			result = new ModelAndView("concept/create");
			result.addObject("concept", concept);
		}

		return result;
	}
}