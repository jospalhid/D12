
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.SmsService;
import domain.Sms;

@Controller
@RequestMapping("/sms")
public class SmsController extends AbstractController {

	@Autowired
	private SmsService	smsService;


	//	@Autowired
	//	private CrownService		crownService;
	//	@Autowired
	//	private ModeratorService	moderatorService;

	// Constructors -----------------------------------------------------------

	public SmsController() {
		super();
	}

	// Actions ---------------------------------------------------------------

	@RequestMapping(value = "/received", method = RequestMethod.GET)
	public ModelAndView listReceived() {
		ModelAndView result;

		final Collection<Sms> sms = this.smsService.findMyReceivedMessages(LoginService.getPrincipal().getId());

		result = new ModelAndView("sms/list");
		result.addObject("sms", sms);
		result.addObject("requestURI", "/sms/received.do");

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView listSend() {
		ModelAndView result;

		final Collection<Sms> sms = this.smsService.findMySendMessages(LoginService.getPrincipal().getId());

		result = new ModelAndView("sms/list");
		result.addObject("sms", sms);
		result.addObject("requestURI", "/sms/send.do");

		return result;
	}

}
