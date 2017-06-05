package controllers;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CreditCardService;
import services.CrownService;
import domain.CreditCard;
import domain.Crown;

@Controller
@RequestMapping("/crown")
public class CrownController extends AbstractController{
	
	// Service
	@Autowired
	private CrownService crownService;
	@Autowired
	private CreditCardService creditCardService;

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
	
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView pay() {
		ModelAndView result;
		Crown crown = crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		CreditCard card = crown.getCreditCard();

		try{
			if (card != null) {
				final int year = Calendar.getInstance().get(Calendar.YEAR);
				final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
				if ((card.getExpirationYear() + 2000) > year || (card.getExpirationYear() + 2000) == year && card.getExpirationMonth() >= month) {
					crown.setAmount(0);
					Crown res= crownService.save(crown);
					result = new ModelAndView("actor/display");
					result.addObject("actor", res);
					result.addObject("message", "crown.pay.success");
				}else{
					result = new ModelAndView("actor/display");
					result.addObject("actor", crown);
					result.addObject("message", "crown.invalid.creditCard");
				}
			}else {
				result = new ModelAndView("creditCard/create");
				result.addObject("creditCard", this.creditCardService.create(crown));
				result.addObject("message", "crown.invalid.creditCard");
			}
		}catch(Throwable oops){
			result = new ModelAndView("actor/display");
			result.addObject("actor", crown);
			result.addObject("message", "crown.pay.error");
		}
		return result;
	}
}
