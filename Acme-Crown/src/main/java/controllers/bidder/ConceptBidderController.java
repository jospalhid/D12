/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.bidder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BidService;
import services.BidderService;
import services.ConceptService;
import controllers.AbstractController;
import domain.Bid;
import domain.Bidder;
import domain.Concept;

@Controller
@RequestMapping("/concept/bidder")
public class ConceptBidderController extends AbstractController {
	
	@Autowired
	private ConceptService conceptService;
	@Autowired
	private BidService bidService;
	@Autowired
	private BidderService bidderService;
	
	// Constructors -----------------------------------------------------------

	public ConceptBidderController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping(value="/list")
	public ModelAndView list() {
		ModelAndView result;
		
		Bidder bidder = this.bidderService.findByUserAccountId(LoginService.getPrincipal().getId());
		List<Concept> concepts = new ArrayList<Concept>();
		concepts.addAll(this.conceptService.getAuction());
		
		result = new ModelAndView("concept/auction");
		result.addObject("concepts", concepts);
		result.addObject("requestURI", "/concept/bidder/list.do");
		
		if(!concepts.isEmpty()){
			Bid bid = this.bidService.create(bidder, concepts.get(0));
			result.addObject("bid", bid);
		}

		return result;
	}
	
	@RequestMapping(value = "/bid", method = RequestMethod.POST, params = "bid")
	public ModelAndView reward(@RequestParam int conceptId, final Bid bid, BindingResult binding) {
		ModelAndView result;
		
		Bid res = this.bidService.reconstruct(conceptId, bid, binding);
		Bidder bidder = this.bidderService.findByUserAccountId(LoginService.getPrincipal().getId());
		List<Concept> concepts = new ArrayList<Concept>();
		concepts.addAll(this.conceptService.getAuction());
		
		result = new ModelAndView("concept/auction");
		result.addObject("concepts", concepts);
		result.addObject("requestURI", "/concept/bidder/list.do");
		
		if(!binding.hasErrors()){
			this.bidService.save(res);
			
			if(!concepts.isEmpty()){
				Bid newBid = this.bidService.create(bidder, concepts.get(0));
				result.addObject("bid", newBid);
			}
			
			result.addObject("message", "concept.bid.success");
		}else{
			result.addObject("bid", bid);
		}


		return result;
	}

}