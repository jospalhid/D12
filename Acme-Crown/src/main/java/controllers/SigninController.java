/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BidderService;
import services.CrownService;
import services.ModeratorService;
import domain.Moderator;
import forms.ActorForm;

@Controller
@RequestMapping("/security")
public class SigninController extends AbstractController {

	//-----------Services----------------
	@Autowired
	private CrownService	crownService;
	@Autowired
	private BidderService	bidderService;
	@Autowired
	private ModeratorService moderatorService;
	

	// Constructors -----------------------------------------------------------

	public SigninController() {
		super();
	}

	@RequestMapping(value = "/signin/crown", method = RequestMethod.GET)
	public ModelAndView signinUserCrown() {
		ModelAndView result;
		final ActorForm crown = new ActorForm();

		result = new ModelAndView("security/signin/crown");
		result.addObject("actorForm", crown);
		result.addObject("tipo","crown");

		return result;
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST, params = "crown")
	public ModelAndView userCrown(final ActorForm actor, final BindingResult binding) {
		ModelAndView result;
		ActorForm res = this.crownService.validate(actor, binding);
		if (binding.hasErrors() || res.getName().equals("Pass") || res.getName().equals("Cond")) {
			result = new ModelAndView("security/signin/crown");
			result.addObject("tipo","crown");
			result.addObject("actorForm", actor);
			if (res.getName().equals("Pass"))
				result.addObject("message", "security.password.failed");
			else if (res.getName().equals("Cond"))
				result.addObject("message", "security.condition.failed");
			else
				result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.crownService.reconstructAndSave(res);
				result = new ModelAndView("redirect:login.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("security/signin/crown");
				result.addObject("actorForm", actor);
				result.addObject("tipo","crown");
				result.addObject("message", "security.signin.failed");
			}

		return result;
	}
	
	@RequestMapping(value = "/signin/bidder", method = RequestMethod.GET)
	public ModelAndView signinUserBidder() {
		ModelAndView result;
		final ActorForm bidder = new ActorForm();

		result = new ModelAndView("security/signin/bidder");
		result.addObject("actorForm", bidder);
		result.addObject("tipo","bidder");

		return result;
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST, params = "bidder")
	public ModelAndView userBidder(final ActorForm actor, final BindingResult binding) {
		ModelAndView result;
		ActorForm res = this.bidderService.validate(actor, binding);
		if (binding.hasErrors() || res.getName().equals("Pass") || res.getName().equals("Cond")) {
			result = new ModelAndView("security/signin/bidder");
			result.addObject("tipo","bidder");
			result.addObject("actorForm", actor);
			if (res.getName().equals("Pass"))
				result.addObject("message", "security.password.failed");
			else if (res.getName().equals("Cond"))
				result.addObject("message", "security.condition.failed");
			else
				result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.bidderService.reconstructAndSave(res);
				result = new ModelAndView("redirect:login.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("security/signin/bidder");
				result.addObject("actorForm", actor);
				result.addObject("tipo","bidder");
				result.addObject("message", "security.signin.failed");
			}

		return result;
	}
	
	@RequestMapping(value = "/moderator/signin", method = RequestMethod.GET)
	public ModelAndView signinUserModerator() {
		ModelAndView result;
		final ActorForm moderator = new ActorForm();

		result = new ModelAndView("moderator/signin");
		result.addObject("actorForm", moderator);

		return result;
	}
	
	@RequestMapping(value = "/moderator/signin", method = RequestMethod.POST, params = "moderator")
	public ModelAndView userModerator(final ActorForm actor, final BindingResult binding) {
		ModelAndView result;
		ActorForm res = this.moderatorService.validate(actor, binding);
		if (binding.hasErrors() || res.getName().equals("Pass") || res.getName().equals("Cond")) {
			result = new ModelAndView("moderator/signin");
			result.addObject("actorForm", actor);
			if (res.getName().equals("Pass"))
				result.addObject("message", "security.password.failed");
			else if (res.getName().equals("Cond"))
				result.addObject("message", "security.condition.failed");
			else
				result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.moderatorService.reconstructAndSave(res);
				Collection<Moderator> moderators = this.moderatorService.findAll();
		
				result = new ModelAndView("moderator/list");
				result.addObject("moderators", moderators);
				result.addObject("requestURI", "/moderator/admin/list.do");

			} catch (Throwable oops) {
				result = new ModelAndView("moderator/signin");
				result.addObject("actorForm", actor);
				result.addObject("message", "security.signin.failed");
			}

		return result;
	}
}
