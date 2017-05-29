/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdminService;
import services.BidderService;
import services.CrownService;
import services.ModeratorService;
import services.SmsService;
import domain.Actor;
import domain.Crown;
import domain.Moderator;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	@Autowired
	private CrownService crownService;
	@Autowired
	private ModeratorService moderatorService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private BidderService bidderService;
	@Autowired
	private SmsService smsService;
	
	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") final String name) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("name", name);
		result.addObject("moment", moment);
		
		try{
			Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			if(crown!=null && crown.isBanned()){
				result.addObject("message", "master.page.crown.banned");
			}else{
				Moderator moderator = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
				if(moderator!=null && moderator.isBanned()){
					result.addObject("message", "master.page.crown.banned");
				}
			}
		}catch(Throwable opps){}
		
		try{
			Actor actor = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
			if(actor==null){
				actor = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
			}
			if(actor == null){
				actor = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			}else{
				actor = this.bidderService.findByUserAccountId(LoginService.getPrincipal().getId());
			}
			result.addObject("unread", this.smsService.unreadCount());
		}catch(Throwable ooops){}

		return result;
	}
}
