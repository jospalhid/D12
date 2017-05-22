
package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Message;

@Service
@Transactional
public class MessageService {

	//Managed repository
	@Autowired
	private MessageRepository	messageRepository;


	//Validator
	//	@Autowired
	//	private Validator validator;

	//Constructors
	public MessageService() {
		super();
	}

	//Simple CRUD methods
	public Message create(final Actor sender, final Actor recipient) {
		Assert.notNull(sender, "The sender cannot be null.");
		Assert.notNull(recipient, "The recipient cannot be null.");
		Message res;
		res = new Message();
		res.setSender(sender);
		res.setRecipient(recipient);
		res.setMoment(Calendar.getInstance().getTime());
		return res;
	}

	public Collection<Message> findAll() {
		final Collection<Message> res = this.messageRepository.findAll();
		return res;
	}

	public Message findOne(final int messageId) {
		final Message res = this.messageRepository.findOne(messageId);
		return res;
	}

	public Message save(final Message sms) {
		Assert.notNull(sms, "The message to save cannot be null.");

		Assert.isTrue(sms.getSender().getUserAccount().equals(LoginService.getPrincipal()), "You are not the owner of this message");

		Assert.notNull(sms.getMoment(), "The message to save cannot have 'moment' null.");
		Assert.notNull(sms.getSubject(), "The message to save cannot have 'subject' null.");
		Assert.isTrue(sms.getSubject() != "", "The message to save cannot have 'subject' blank.");
		Assert.notNull(sms.getBody(), "The message to save cannot have 'body' null.");

		final Message res = this.messageRepository.save(sms);
		res.getRecipient().getReceivedMessages().add(res);
		res.getSender().getSendMessages().add(res);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public void delete(final Message sms) {
		Assert.notNull(sms, "The message to delete cannot be null.");
		Assert.isTrue(this.messageRepository.exists(sms.getId()));

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(sms.getSender().getUserAccount().equals(ua) || sms.getRecipient().getUserAccount().equals(ua), "You are not the owner of the message");

		this.messageRepository.delete(sms);
	}

	//----------Other Methods------------------------
	public Collection<Message> findMyReceivedMessages(final int uaId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		return this.messageRepository.findMyReceivedMessages(ua.getId());
	}

	public Collection<Message> findMySentChirps(final int uaId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		return this.messageRepository.findMySendMessages(ua.getId());
	}
}
