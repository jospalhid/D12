
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

import repositories.CrownRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Comment;
import domain.Concept;
import domain.Crown;
import domain.Moderator;
import domain.Project;
import domain.Reward;
import domain.Sms;
import forms.ActorForm;

@Service
@Transactional
public class CrownService {

	//Managed repository
	@Autowired
	private CrownRepository		crownRepository;

	//Validator
	@Autowired
	private Validator			validator;

	//Supporting services
	@Autowired
	private ModeratorService	moderatorService;
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private ConfigService		configService;


	//Constructors
	public CrownService() {
		super();
	}

	//Simple CRUD methods
	public Crown create(final UserAccount ua) {
		Assert.notNull(ua);
		Crown res;
		res = new Crown();
		res.setUserAccount(ua);
		res.setFavs(new ArrayList<Project>());
		res.setProjects(new ArrayList<Project>());
		res.setRewards(new ArrayList<Reward>());
		res.setPostComments(new ArrayList<Comment>());
		res.setReceivedMessages(new ArrayList<Sms>());
		res.setSendMessages(new ArrayList<Sms>());
		res.setConcepts(new ArrayList<Concept>());
		res.setAmount(0);
		return res;
	}

	public Collection<Crown> findAll() {
		final Collection<Crown> res = this.crownRepository.findAll();
		return res;
	}

	public Crown findOne(final int crownId) {
		final Crown res = this.crownRepository.findOne(crownId);
		return res;
	}

	public Crown save(final Crown crown) {
		Assert.notNull(crown, "The crown to save cannot be null.");

		final Crown res = this.crownRepository.save(crown);
		return res;
	}

	public void delete(final Crown crown) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(crown, "The chorbi to delete cannot be null.");
		Assert.isTrue(this.crownRepository.exists(crown.getId()));

		Assert.isTrue(crown.getProjects().isEmpty(), "The crown cannot be delete with projects");
		Assert.isTrue(crown.getRewards().isEmpty(), "The crown cannot be delete with rewards");
		Assert.isTrue(crown.getConcepts().isEmpty(), "Cannot be delete with concepts");
		Assert.isTrue(crown.getReceivedMessages().isEmpty(), "Cannot delete with messages");
		Assert.isTrue(crown.getSendMessages().isEmpty(), "Cannot delete with messages");

		this.crownRepository.delete(crown);
	}

	//Utilites methods
	public Crown findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.crownRepository.findByUserAccountId(id);
	}

	public Collection<Crown> findAllNotBanned() {
		return this.crownRepository.findAllNotBanned();
	}

	public void ban(final int crownId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.MODERATOR);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a moderator for this action.");
		final Moderator moderator = this.moderatorService.findByUserAccountId(ua.getId());
		Assert.isTrue(moderator.getLevel() == 2, "You need the level 2 for this action");

		final Crown crown = this.findOne(crownId);
		crown.setBanned(true);
		final Authority b = new Authority();
		b.setAuthority(Authority.BANNED);
		crown.getUserAccount().setAuthorities(new ArrayList<Authority>());
		crown.getUserAccount().addAuthority(b);
		this.save(crown);
	}

	public void unban(final int crownId) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.MODERATOR);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a moderator for this action.");
		final Moderator moderator = this.moderatorService.findByUserAccountId(ua.getId());
		Assert.isTrue(moderator.getLevel() == 2, "You need the level 2 for this action");

		final Crown crown = this.findOne(crownId);
		crown.setBanned(false);
		final Authority c = new Authority();
		c.setAuthority(Authority.CROWN);
		crown.getUserAccount().setAuthorities(new ArrayList<Authority>());
		crown.getUserAccount().addAuthority(c);
		this.save(crown);
	}

	public Crown reconstructAndSave(final ActorForm actor) {
		Crown result;
		final UserAccount ua = this.userAccountService.create();

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(actor.getPassword1(), null);
		
		ua.setUsername(actor.getUsername());
		ua.setPassword(hash);

		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		ua.getAuthorities().add(a);

		result = this.create(ua);

		result.setName(actor.getName());
		result.setSurname(actor.getSurname());
		result.setEmail(actor.getEmail());
		result.setPhone(actor.getPhone());
		result.setPicture(actor.getPicture());

		return this.save(result);
	}

	public void auction() {
		final Crown crown = this.findByUserAccountId(LoginService.getPrincipal().getId());
		crown.setAmount(crown.getAmount() + this.configService.find().getAuction());
		this.save(crown);

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
	
	public Crown validateAndEdit(Crown crown, BindingResult binding) {
		Crown c = this.findByUserAccountId(LoginService.getPrincipal().getId());
		Crown res = this.create(c.getUserAccount());
		res.setName(crown.getName());
		res.setSurname(crown.getSurname());
		res.setEmail(crown.getEmail());
		res.setPhone(crown.getPhone());
		res.setPicture(crown.getPicture());
		
		validator.validate(res, binding);
		
		return crown;
	}
	
	public Crown reconstructAndEdit(Crown crown) {
		Crown res = this.findByUserAccountId(LoginService.getPrincipal().getId());

		res.setName(crown.getName());
		res.setSurname(crown.getSurname());
		res.setEmail(crown.getEmail());
		res.setPhone(crown.getPhone());
		res.setPicture(crown.getPicture());
		

		return this.save(res);
	}

}
