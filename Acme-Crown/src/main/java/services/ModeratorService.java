package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ModeratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Crown;
import domain.Moderator;
import domain.Sms;
import forms.ActorForm;

@Service
@Transactional
public class ModeratorService {

	//Managed repository
	@Autowired
	private ModeratorRepository	moderatorRepository;


	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services
	@Autowired
	private CrownService crownService;
	@Autowired
	private UserAccountService userAccountService;

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
		res.setLevel(1);
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
		Assert.notNull(moderator, "The moderator to save cannot be null.");
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

	public Moderator crownToMod(int crownId) {
		final UserAccount uax = LoginService.getPrincipal();
		Assert.notNull(uax);
		final Authority az = new Authority();
		az.setAuthority(Authority.ADMIN);
		Assert.isTrue(uax.getAuthorities().contains(az), "You must to be a admin for this action");
		
		Crown crown = this.crownService.findOne(crownId);
		
		Moderator result;
		final UserAccount ua = this.userAccountService.create();

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(crown.getUserAccount().getUsername()+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.MONTH), null);
		
		ua.setUsername(crown.getUserAccount().getUsername()+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.MONTH));
		ua.setPassword(hash);

		final Authority a = new Authority();
		a.setAuthority(Authority.MODERATOR);
		ua.getAuthorities().add(a);

		result = this.create(ua);

		result.setName(crown.getName());
		result.setSurname(crown.getSurname());
		result.setEmail(crown.getEmail());
		result.setPhone(crown.getPhone());
		result.setPicture(crown.getPicture());
		result.setCrown(crown);
		
		Moderator res = this.save(result);
		
		return res;
	}
	
	public ActorForm validate(ActorForm actor, BindingResult binding) {
		validator.validate(actor, binding);
		List<String> cond = Arrays.asList(actor.getConditions());
		ActorForm res;
		if (!actor.getPassword1().equals(actor.getPassword2()) || !cond.contains("acepto")) {
			res = new ActorForm();
			res.setName("Pass");
			if (!cond.contains("acepto"))
				res.setName("Cond");
		}else{
			res = actor;
		}
		return res;
	}
	
	public Moderator reconstructAndSave(final ActorForm actor) {
		Moderator result;
		final UserAccount ua = this.userAccountService.create();

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(actor.getPassword1(), null);
		
		ua.setUsername(actor.getUsername());
		ua.setPassword(hash);

		final Authority a = new Authority();
		a.setAuthority(Authority.MODERATOR);
		ua.getAuthorities().add(a);

		result = this.create(ua);

		result.setName(actor.getName());
		result.setSurname(actor.getSurname());
		result.setEmail(actor.getEmail());
		result.setPhone(actor.getPhone());
		result.setPicture(actor.getPicture());

		return this.save(result);
	}

}