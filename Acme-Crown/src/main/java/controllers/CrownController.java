package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CrownService;
import domain.Crown;

@Controller
@RequestMapping("/crown")
public class CrownController extends AbstractController{
	
	// Service
	@Autowired
	private CrownService crownService;

	// Constructors -----------------------------------------------------------

	public CrownController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result = new ModelAndView("crown/edit");
		Crown crown = crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		result.addObject("crown", crown);
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCrown(Crown crown, BindingResult binding) {
		ModelAndView result;
		Crown res = this.crownService.validateAndEdit(crown, binding);
		
		if(binding.hasErrors()){
			result = new ModelAndView("crown/edit");
			result.addObject("crown", crown);
			result.addObject("errors", binding.getAllErrors());
		}else{
			try {
				this.crownService.reconstructAndEdit(res);
				result = new ModelAndView("crown/edit");
				result.addObject("crown", res);
				result.addObject("message", "crown.commit.success");
			} catch (final Throwable oops) {
				result = new ModelAndView("crown/edit");
				result.addObject("crown", res);
				result.addObject("message", "crown.commit.error");
			}
		}
		return result;
	}
}
