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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CrownService;
import forms.ActorForm;

@Controller
@RequestMapping("/security")
public class SigninController extends AbstractController {

	//-----------Services----------------
	@Autowired
	private CrownService	crownService;


	// Constructors -----------------------------------------------------------

	public SigninController() {
		super();
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signinUser() {
		ModelAndView result;
		final ActorForm crown = new ActorForm();

		result = new ModelAndView("security/signin");
		result.addObject("actorForm", crown);

		return result;
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST, params = "signin")
	public ModelAndView user(final ActorForm actor, final BindingResult binding) {
		ModelAndView result;
//		Crown crown = this.crownService.reconstruct(actor, binding);
		ActorForm res = this.crownService.validate(actor, binding);
		if (binding.hasErrors() || res.getName().equals("Pass") || res.getName().equals("Cond")) {
			result = new ModelAndView("security/signin");
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
				result = new ModelAndView("security/signin");
				result.addObject("actorForm", actor);
				result.addObject("message", "security.signin.failed");
			}

		return result;
	}

}
