package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BidderService;
import services.CrownService;
import domain.Actor;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController{
	
	// Service
	@Autowired
	private CrownService crownService;
	@Autowired
	private BidderService bidderService;

	// Constructors -----------------------------------------------------------

	public ActorController() {
		super();
	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Actor actor = crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		if(actor == null)
			actor = bidderService.findByUserAccountId(LoginService.getPrincipal().getId());
		
		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		
		return result;
	}
	
}
