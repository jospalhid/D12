package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BidderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Bid;
import domain.Bidder;
import domain.Sms;
import forms.ActorForm;

@Service
@Transactional
public class BidderService {

	//Managed repository
	@Autowired
	private BidderRepository	bidderRepository;


	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services
	@Autowired
	private UserAccountService userAccountService;

	//Constructors
	public BidderService() {
		super();
	}

	//Simple CRUD methods
	public Bidder create(final UserAccount ua) {
		Assert.notNull(ua);
		Bidder res;
		res = new Bidder();
		res.setUserAccount(ua);
		res.setReceivedMessages(new ArrayList<Sms>());
		res.setSendMessages(new ArrayList<Sms>());
		res.setBids(new ArrayList<Bid>());
		return res;
	}

	public Collection<Bidder> findAll() {
		final Collection<Bidder> res = this.bidderRepository.findAll();
		return res;
	}

	public Bidder findOne(final int bidderId) {
		final Bidder res = this.bidderRepository.findOne(bidderId);
		return res;
	}

	public Bidder save(final Bidder bidder) {
		Assert.notNull(bidder, "The bidder to save cannot be null.");
		
		final Bidder res = this.bidderRepository.save(bidder);
		return res;
	}

	public void delete(final Bidder bidder) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(bidder, "The bidder to delete cannot be null.");
		Assert.isTrue(this.bidderRepository.exists(bidder.getId()));
		
		Assert.isTrue(bidder.getBids().isEmpty(), "Cannot be delete with bids");
		Assert.isTrue(bidder.getReceivedMessages().isEmpty(), "Cannot delete with messages");
		Assert.isTrue(bidder.getSendMessages().isEmpty(), "Cannot delete with messages");
		
		this.bidderRepository.delete(bidder);
	}

	//Utilites methods
	public Bidder findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.bidderRepository.findByUserAccountId(id);
	}
	
	public Bidder reconstructAndSave(final ActorForm actor) {
		Bidder result;
		final UserAccount ua = this.userAccountService.create();

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(actor.getPassword1(), null);
		
		ua.setUsername(actor.getUsername());
		ua.setPassword(hash);

		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		ua.getAuthorities().add(a);

		result = this.create(ua);

		result.setName(actor.getName());
		result.setSurname(actor.getSurname());
		result.setEmail(actor.getEmail());
		result.setPhone(actor.getPhone());
		result.setPicture(actor.getPicture());

		return this.save(result);
	}
	
	public ActorForm validate(ActorForm actor, BindingResult binding) {
		validator.validate(actor, binding);
		List<String> cond = Arrays.asList(actor.getConditions());
		if (!actor.getPassword1().equals(actor.getPassword2()) || !cond.contains("acepto")) {
			actor.setName("Pass");
			if (!cond.contains("acepto"))
				actor.setName("Cond");
		}
		return actor;
	}


}