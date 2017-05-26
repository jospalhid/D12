package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BidderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Bid;
import domain.Bidder;
import domain.Sms;

@Service
@Transactional
public class BidderService {

	//Managed repository
	@Autowired
	private BidderRepository	bidderRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

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

}