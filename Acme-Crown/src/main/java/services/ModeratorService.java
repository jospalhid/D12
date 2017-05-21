package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ModeratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Crown;
import domain.Moderator;

@Service
@Transactional
public class ModeratorService {

	//Managed repository
	@Autowired
	private ModeratorRepository	moderatorRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

	//Constructors
	public ModeratorService() {
		super();
	}

	//Simple CRUD methods
	public Moderator create(final UserAccount ua) {
		Assert.notNull(ua);
		Moderator res;
		res = new Moderator();
		res.setUserAccount(ua);
		res.setBanned(false);
		return res;
	}

	public Collection<Moderator> findAll() {
		final Collection<Moderator> res = this.moderatorRepository.findAll();
		return res;
	}

	public Moderator findOne(final int moderatorId) {
		final Moderator res = this.moderatorRepository.findOne(moderatorId);
		return res;
	}

	public Moderator save(final Moderator moderator) {
		Assert.notNull(moderator, "The crown to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to create a moderator.");
		
		final Moderator res = this.moderatorRepository.save(moderator);
		return res;
	}

	public void delete(final Moderator moderator) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(moderator, "The chorbi to delete cannot be null.");
		Assert.isTrue(this.moderatorRepository.exists(moderator.getId()));

		this.moderatorRepository.delete(moderator);
	}

	//Utilites methods
	public Crown findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.moderatorRepository.findByUserAccountId(id);
		}

}