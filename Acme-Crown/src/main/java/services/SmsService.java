
package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SmsRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Sms;

@Service
@Transactional
public class SmsService {

	//Managed repository
	@Autowired
	private SmsRepository		smsRepository;

	@Autowired
	private CrownService		crownService;
	@Autowired
	private ModeratorService	moderatorService;
	@Autowired
	private AdminService		adminService;

	//Validator
	@Autowired
	private Validator			validator;


	//Constructors
	public SmsService() {
		super();
	}

	//Simple CRUD methods
	public Sms create(final Actor sender, final Actor recipient) {
		Assert.notNull(sender, "The sender cannot be null.");
		Assert.notNull(recipient, "The recipient cannot be null.");
		Sms res;
		res = new Sms();
		res.setSender(sender);
		res.setRecipient(recipient);
		res.setMoment(Calendar.getInstance().getTime());
		res.setReaded(false);
		return res;
	}

	public Collection<Sms> findAll() {
		final Collection<Sms> res = this.smsRepository.findAll();
		return res;
	}

	public Sms findOne(final int smsId) {
		final Sms res = this.smsRepository.findOne(smsId);
		return res;
	}

	public Sms save(final Sms sms) {
		Assert.notNull(sms, "The message to save cannot be null.");

		Assert.isTrue(sms.getSender().getUserAccount().equals(LoginService.getPrincipal()), "You are not the owner of this message");

		Assert.notNull(sms.getMoment(), "The message to save cannot have 'moment' null.");
		Assert.notNull(sms.getSubject(), "The message to save cannot have 'subject' null.");
		Assert.isTrue(sms.getSubject() != "", "The message to save cannot have 'subject' blank.");
		Assert.notNull(sms.getBody(), "The message to save cannot have 'body' null.");

		final Sms res = this.smsRepository.save(sms);
		res.getRecipient().getReceivedMessages().add(res);
		res.getSender().getSendMessages().add(res);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public void delete(final Sms sms) {
		Assert.notNull(sms, "The message to delete cannot be null.");
		Assert.isTrue(this.smsRepository.exists(sms.getId()));

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(sms.getSender().getUserAccount().equals(ua) || sms.getRecipient().getUserAccount().equals(ua), "You are not the owner of the message");

		this.smsRepository.delete(sms);
	}

	//----------Other Methods------------------------
	public Collection<Sms> findMyReceivedMessages(final int uaId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		return this.smsRepository.findMyReceivedMessages(ua.getId());
	}

	public Collection<Sms> findMySendMessages(final int uaId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		return this.smsRepository.findMySendMessages(ua.getId());
	}

	public void setReaded(final int smsId) {
		final Sms sms = this.smsRepository.findOne(smsId);
		sms.setReaded(true);
		this.smsRepository.save(sms);
	}

	public Sms reconstruct(final Sms sms, final BindingResult binding) {

		Actor sender = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (sender == null)
			sender = this.moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
		if (sender == null)
			sender = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());

		final Sms res = this.create(sender, sms.getRecipient());
		res.setMoment(Calendar.getInstance().getTime());
		res.setSubject(sms.getSubject());
		res.setBody(sms.getBody());

		this.validator.validate(res, binding);

		return res;
	}
}
