package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ModeratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Moderator;
import domain.Sms;

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
		res.setReceivedMessages(new ArrayList<Sms>());
		res.setSendMessages(new ArrayList<Sms>());
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
		
		Assert.isTrue(moderator.getReceivedMessages().isEmpty(), "Cannot delete with messages");
		Assert.isTrue(moderator.getSendMessages().isEmpty(), "Cannot delete with messages");

		this.moderatorRepository.delete(moderator);
	}

	//Utilites methods
	public Moderator findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.moderatorRepository.findByUserAccountId(id);
	}
	
	public Collection<Moderator> findAllNotBanned(){
		return this.moderatorRepository.findAllNotBanned();
	}

	public void ban(int moderatorId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin for this action");
		
		Moderator moderator = this.findOne(moderatorId);
		moderator.setBanned(true);
		Authority b = new Authority();
		b.setAuthority(Authority.BANNED);
		moderator.getUserAccount().setAuthorities(new ArrayList<Authority>());
		moderator.getUserAccount().addAuthority(b);
		this.save(moderator);
		
	}

	public void unban(int moderatorId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin for this action");
		
		Moderator moderator = this.findOne(moderatorId);
		moderator.setBanned(false);
		Authority b = new Authority();
		b.setAuthority(Authority.MODERATOR);
		moderator.getUserAccount().setAuthorities(new ArrayList<Authority>());
		moderator.getUserAccount().addAuthority(b);
		this.save(moderator);
		
	}

	public void level(int moderatorId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin for this action");
		
		Moderator moderator = this.findOne(moderatorId);
		if(moderator.getLevel()==1){
			moderator.setLevel(2);
		}else{
			moderator.setLevel(1);
		}
		
		this.save(moderator);
	}

}