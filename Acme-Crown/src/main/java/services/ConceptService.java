package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
//	@Autowired
//	private Validator validator;
	
	//Supporting services

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
		res.setValid(false);
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

	public void delete(final Concept concept) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete a contest.");

		Assert.notNull(concept, "The concept to delete cannot be null.");
		Assert.isTrue(this.conceptRepository.exists(concept.getId()));
		
		Assert.isTrue(concept.getBids().isEmpty(), "Cannot be delete with bids");

		this.conceptRepository.delete(concept);
	}

	//Utilites methods

}