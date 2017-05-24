package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CreditCardRepository;
import security.LoginService;
import security.UserAccount;
import domain.CreditCard;
import domain.Crown;
import domain.SuperUser;

@Service
@Transactional
public class CreditCardService {

	//Managed repository
	@Autowired
	private CreditCardRepository	creditCardRepository;


	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services
	@Autowired
	private CrownService crownService;
	
	//Constructors
	public CreditCardService() {
		super();
	}

	//Simple CRUD methods
	public CreditCard create(final SuperUser superUser) {
		Assert.notNull(superUser);
		CreditCard res;
		res = new CreditCard();
		res.setSuperUser(superUser);
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
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		
		Assert.isTrue((card.getExpirationYear()+2000)>year || (card.getExpirationYear()+2000)==year && card.getExpirationMonth()>month,"The credit card is invalid");
		
		final CreditCard res = this.creditCardRepository.save(card);
		return res;
	}

	public void delete(final CreditCard card) {
		final UserAccount ua = LoginService.getPrincipal();

		Assert.notNull(card, "The card to delete cannot be null.");
		Assert.isTrue(this.creditCardRepository.exists(card.getId()));

		Assert.isTrue(card.getSuperUser().getUserAccount().equals(ua),"You are not the owner of this card");
		
		this.creditCardRepository.delete(card);
	}

	public CreditCard validate(CreditCard creditCard, BindingResult binding) {
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		CreditCard res =this.create(crown);
		res.setHolder(creditCard.getHolder());
		res.setBrand(creditCard.getBrand());
		res.setNumber(creditCard.getNumber());
		res.setExpirationMonth(creditCard.getExpirationMonth());
		res.setExpirationYear(creditCard.getExpirationYear());
		res.setCvv(creditCard.getCvv());
		
		validator.validate(res, binding);
		
		return res;
	}
	
	public CreditCard reconstructAndSave(CreditCard creditCard){
		Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
		CreditCard res = crown.getCreditCard();
		if(res!=null){
			res.setHolder(creditCard.getHolder());
			res.setBrand(creditCard.getBrand());
			res.setNumber(creditCard.getNumber());
			res.setExpirationMonth(creditCard.getExpirationMonth());
			res.setExpirationYear(creditCard.getExpirationYear());
			res.setCvv(creditCard.getCvv());
		}
		CreditCard fin=this.save(res);
		return fin;
	}

	//Utilites methods

}