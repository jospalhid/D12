package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import security.LoginService;
import security.UserAccount;
import domain.CreditCard;
import domain.Crown;

@Service
@Transactional
public class CreditCardService {

	//Managed repository
	@Autowired
	private CreditCardRepository	creditCardRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

	//Constructors
	public CreditCardService() {
		super();
	}

	//Simple CRUD methods
	public CreditCard create(final Crown crown) {
		CreditCard res;
		res = new CreditCard();
		res.setCrown(crown);
		return res;
	}

	public Collection<CreditCard> findAll() {
		final Collection<CreditCard> res = this.creditCardRepository.findAll();
		return res;
	}

	public CreditCard findOne(final int ccId) {
		final CreditCard res = this.creditCardRepository.findOne(ccId);
		return res;
	}

	public CreditCard save(final CreditCard card) {
		Assert.notNull(card, "The crown to save cannot be null.");
		
		final CreditCard res = this.creditCardRepository.save(card);
		return res;
	}

	public void delete(final CreditCard card) {
		final UserAccount ua = LoginService.getPrincipal();

		Assert.notNull(card, "The card to delete cannot be null.");
		Assert.isTrue(this.creditCardRepository.exists(card.getId()));

		Assert.isTrue(card.getCrown().getUserAccount().equals(ua),"You are not the owner of this card");
		
		this.creditCardRepository.delete(card);
	}

	//Utilites methods

}