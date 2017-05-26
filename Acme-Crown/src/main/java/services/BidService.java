package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BidRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Bid;
import domain.Bidder;
import domain.Concept;

@Service
@Transactional
public class BidService {

	//Managed repository
	@Autowired
	private BidRepository	bidRepository;

	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services
	@Autowired
	private BidderService bidderService;
	@Autowired
	private ConceptService conceptService;

	//Constructors
	public BidService() {
		super();
	}

	//Simple CRUD methods
	public Bid create(Bidder bidder, Concept concept) {
		Assert.notNull(bidder);
		Assert.notNull(concept);
		Bid res;
		res = new Bid();
		res.setBidder(bidder);
		res.setConcept(concept);
		res.setMoment(Calendar.getInstance().getTime());
		return res;
	}

	public Collection<Bid> findAll() {
		final Collection<Bid> res = this.bidRepository.findAll();
		return res;
	}

	public Bid findOne(final int bidId) {
		final Bid res = this.bidRepository.findOne(bidId);
		return res;
	}

	public Bid save(final Bid bid) {
		Assert.notNull(bid, "The bid to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder to create a bid.");
		Assert.isTrue(!this.getAllBids(bid.getConcept().getId()).contains(bid.getInput()),"No equal bids");
		if(bid.getConcept().getDay()!=null){
			Calendar dateB =Calendar.getInstance();
			dateB.setTime(bid.getMoment());
			int dayB=dateB.get(Calendar.DAY_OF_MONTH);
			int monthB=dateB.get(Calendar.MONTH)+1;
			int yearB=dateB.get(Calendar.YEAR);
			int hour = dateB.get(Calendar.HOUR_OF_DAY);
			
			Calendar dateC =Calendar.getInstance();
			dateC.setTime(bid.getConcept().getDay());
			int dayC=dateC.get(Calendar.DAY_OF_MONTH);
			int monthC=dateC.get(Calendar.MONTH)+1;
			int yearC=dateC.get(Calendar.YEAR);
			//concept.ttl-currentHour+1
			Assert.isTrue(dayB==dayC && monthB==monthC && yearC==yearB && bid.getConcept().getTtl()-hour+1>=0,"The bid must be in time"); //3,6e+6
		}
		
		final Bid res = this.bidRepository.save(bid);
		return res;
	}

	public void delete(final Bid bid) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder to delete a bid.");

		Assert.notNull(bid, "The bid to delete cannot be null.");
		Assert.isTrue(this.bidRepository.exists(bid.getId()));
		
		this.bidRepository.delete(bid);
	}

	public Bid reconstruct(int conceptId, Bid bid, BindingResult binding) {
		Bidder bidder = this.bidderService.findByUserAccountId(LoginService.getPrincipal().getId());
		Concept concept = this.conceptService.findOne(conceptId);
		Bid res = this.create(bidder, concept);
		res.setInput(bid.getInput());
		
		validator.validate(res, binding);
		
		return res;
	}

	//Utilites methods
	
	public Collection<Double> getAllBids(int id){
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder for this action");
		
		return this.bidRepository.getAllBids(id);
	}

}