package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConceptRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Bid;
import domain.Concept;
import domain.Crown;

@Service
@Transactional
public class ConceptService {

	//Managed repository
	@Autowired
	private ConceptRepository	conceptRepository;


	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services
	@Autowired
	private CrownService crownService;

	//Constructors
	public ConceptService() {
		super();
	}

	//Simple CRUD methods
	public Concept create(Crown crown) {
		Assert.notNull(crown);
		Concept res;
		res = new Concept();
		res.setCrown(crown);
		res.setBids(new ArrayList<Bid>());
		res.setValid(0);
		return res;
	}

	public Collection<Concept> findAll() {
		final Collection<Concept> res = this.conceptRepository.findAll();
		return res;
	}

	public Concept findOne(final int conceptId) {
		final Concept res = this.conceptRepository.findOne(conceptId);
		return res;
	}

	public Concept save(final Concept concept) {
		Assert.notNull(concept, "The concept to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to create a concept.");
		
		final Concept res = this.conceptRepository.save(concept);
		return res;
	}
	
	public Concept saveAndValid(final Concept concept) {
		Assert.notNull(concept);
		
		final Concept res = this.conceptRepository.save(concept);
		return res;
	}

	public void delete(final Concept concept) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to delete a contest.");

		Assert.notNull(concept, "The concept to delete cannot be null.");
		Assert.isTrue(this.conceptRepository.exists(concept.getId()));
		
		Assert.isTrue(concept.getBids().isEmpty(), "Cannot be delete with bids");

		this.conceptRepository.delete(concept);
	}

	//Utilites methods
	public Collection<Concept> findMyConcept(){
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown for this action.");
		
		return this.conceptRepository.findMyConcept(ua.getId());
	}
	
	public Collection<Concept> findNotValidate(){
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin for this action.");
		
		return this.conceptRepository.findNotValid();
	}
	
	public Collection<Concept> getAuction(){
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder for this action.");
		
		return this.conceptRepository.getAuction();
	}

	public Concept reconstruct(Concept concept, BindingResult binding) {
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		Concept res = this.create(crown);
		res.setTitle(concept.getTitle());
		res.setDescripcion(concept.getDescripcion());
		res.setTtl(concept.getTtl());
		res.setDibs(concept.getDibs());
		
		validator.validate(res, binding);
		
		return res;
	}

	public void valid(int conceptId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin for this action.");
		
		Concept concept = this.findOne(conceptId);
		
		concept.setValid(1);
		Calendar date = Calendar.getInstance();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1;
		date.set(year, month, day);
		
		concept.setDay(date.getTime());
		
		this.saveAndValid(concept);
	}

	public void invalid(int conceptId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin for this action.");
		
		Concept concept = this.findOne(conceptId);
		
		concept.setValid(2);
		
		this.saveAndValid(concept);
		
	}
	
	public Integer getActiveConcept(int id){
		return this.conceptRepository.getActiveConcept(id);
	}
	
	public Collection<Concept> findMyWins(){
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.BIDDER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a bidder for this action.");
		
		return this.conceptRepository.findMyWins(ua.getId());
	}

}