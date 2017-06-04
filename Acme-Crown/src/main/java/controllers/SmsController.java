
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdminService;
import services.BidderService;
import services.CrownService;
import services.ModeratorService;
import services.SmsService;
import domain.Actor;
import domain.Sms;

@Controller
@RequestMapping("/sms")
public class SmsController extends AbstractController {

	@Autowired
	private SmsService			smsService;

	@Autowired
	private CrownService		crownService;
	@Autowired
	private ModeratorService	moderatorService;
	@Autowired
	private AdminService		adminService;
	@Autowired
	private BidderService bidderService;


	// Constructors -----------------------------------------------------------

	public SmsController() {
		super();
	}

	// Actions ---------------------------------------------------------------

	@RequestMapping(value = "/received", method = RequestMethod.GET)
	public ModelAndView listReceived() {
		ModelAndView result;

		final Collection<Sms> sms = this.smsService.findMyReceivedMessages(LoginService.getPrincipal().getId());

		result = new ModelAndView("sms/received");
		result.addObject("sms", sms);
		result.addObject("requestURI", "/sms/received.do");

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView listSend() {
		ModelAndView result;

		final Collection<Sms> sms = this.smsService.findMySendMessages(LoginService.getPrincipal().getId());

		result = new ModelAndView("sms/sent");
		result.addObject("sms", sms);
		result.addObject("requestURI", "/sms/send.do");

		return result;
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int smsId) {
		ModelAndView result;

		final Sms sms = this.smsService.findOne(smsId);
		this.smsService.setReaded(smsId);
		result = new ModelAndView("sms/display");
		result.addObject("sms", sms);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		Actor sender = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (sender == null)
			sender = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (sender == null)
			sender = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (sender == null)
			sender = this.bidderService.findByUserAccountId(LoginService.getPrincipal().getId());

		final Sms res = this.smsService.create(sender, sender);
		final Collection<Actor> actors = new ArrayList<Actor>();
		actors.addAll(this.crownService.findAllNotBanned());
		actors.addAll(this.moderatorService.findAllNotBanned());
		actors.addAll(this.adminService.findAll());
		actors.addAll(this.bidderService.findAll());

		result = new ModelAndView("sms/create");
		result.addObject("sms", res);
		result.addObject("actors", actors);

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "save")
	public ModelAndView sendSms(Sms sms, final BindingResult binding) {
		ModelAndView result;
		final Sms res = this.smsService.reconstruct(sms, binding);

		if (!binding.hasErrors())
			try {
				this.smsService.save(res);
				final Collection<Sms> smss = this.smsService.findMySendMessages(LoginService.getPrincipal().getId());
				result = new ModelAndView("sms/sent");
				result.addObject("sms", smss);
				result.addObject("message", "sms.commit.success");
			} catch (final Throwable opps) {
				final Collection<Actor> actors = new ArrayList<Actor>();
				actors.addAll(this.crownService.findAllNotBanned());
				actors.addAll(this.moderatorService.findAllNotBanned());
				actors.addAll(this.adminService.findAll());
				actors.addAll(this.bidderService.findAll());
				
				result = new ModelAndView("sms/create");
				result.addObject("sms", sms);
				result.addObject("actors", actors);
				result.addObject("message", "sms.commit.error");
			}
		else {
			final Collection<Actor> actors = new ArrayList<Actor>();
			actors.addAll(this.crownService.findAll());
			actors.addAll(this.moderatorService.findAll());
			actors.addAll(this.adminService.findAll());
			actors.addAll(this.bidderService.findAll());
			
			result = new ModelAndView("sms/create");
			result.addObject("sms", sms);
			result.addObject("actors", actors);
		}

		return result;
	}

}
