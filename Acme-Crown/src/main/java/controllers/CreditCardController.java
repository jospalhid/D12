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
@RequestMapping("/creditCard")
public class CreditCardController extends AbstractController {
	
	@Autowired
	private CrownService crownService;
	@Autowired
	private CreditCardService creditCardService;
	
	// Constructors -----------------------------------------------------------

	public CreditCardController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping(value="/edit",method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		CreditCard creditCard = crown.getCreditCard();
		result = new ModelAndView("creditCard/edit");
		
		if(creditCard==null){
			creditCard= this.creditCardService.create(crown);
			result = new ModelAndView("creditCard/create");
		}
		
		result.addObject("creditCard", creditCard);

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(CreditCard creditCard, BindingResult binding) {
		ModelAndView result;
		
		CreditCard res= this.creditCardService.validate(creditCard, binding);
		if(!binding.hasErrors()){
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH)+1;
			if((res.getExpirationYear()+2000)<year || (res.getExpirationYear()+2000)==year && res.getExpirationMonth()<=month){
				if(creditCard.getId()==0){
					result = new ModelAndView("creditCard/create");
				}else{
					result = new ModelAndView("creditCard/edit");
				}
				result.addObject("creditCard", creditCard);
				result.addObject("message", "creditCard.commit.expired");
			}else{
				try{
					this.creditCardService.reconstructAndSave(res);
					result = new ModelAndView("creditCard/edit");
					result.addObject("creditCard", creditCard);
					result.addObject("message", "creditCard.commit.success");
				}
				catch(Throwable oops){
					if(creditCard.getId()==0){
						result = new ModelAndView("creditCard/create");
					}else{
						result = new ModelAndView("creditCard/edit");
					}
					result.addObject("creditCard", creditCard);
					result.addObject("message", "creditCard.commit.error");
				}
			}
		}else{
			if(creditCard.getId()==0){
				result = new ModelAndView("creditCard/create");
			}else{
				result = new ModelAndView("creditCard/edit");
			}
			result.addObject("creditCard", creditCard);
		}
		
		return result;
	}
	
}
