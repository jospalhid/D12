package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CrownRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Crown;
import domain.Project;
import domain.Reward;

@Service
@Transactional
public class CrownService {

	//Managed repository
	@Autowired
	private CrownRepository	crownRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

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
		
		this.crownRepository.delete(crown);
	}

	//Utilites methods
	public Crown findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.crownRepository.findByUserAccountId(id);
		}

	public void ban(int crownId) {
		Crown crown = this.findOne(crownId);
		crown.setBanned(true);
		Authority b = new Authority();
		b.setAuthority(Authority.BANNED);
		crown.getUserAccount().setAuthorities(new ArrayList<Authority>());
		crown.getUserAccount().addAuthority(b);
		this.save(crown);
	}

	public void unban(int crownId) {
		Crown crown = this.findOne(crownId);
		crown.setBanned(false);
		Authority c = new Authority();
		c.setAuthority(Authority.CROWN);
		crown.getUserAccount().setAuthorities(new ArrayList<Authority>());
		crown.getUserAccount().addAuthority(c);
		this.save(crown);
	}

}