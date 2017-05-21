package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Config;

@Service
@Transactional
public class ConfigService {

	//Managed repository
	@Autowired
	private ConfigRepository	configRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

	//Constructors
	public ConfigService() {
		super();
	}

	//Simple CRUD methods
	public Config create() {
		Config res;
		res = new Config();
		return res;
	}

	public Collection<Config> findAll() {
		final Collection<Config> res = this.configRepository.findAll();
		return res;
	}

	public Config findOne(final int categoryId) {
		final Config res = this.configRepository.findOne(categoryId);
		return res;
	}
	
	public Config find() {
		final List<Config> res = new ArrayList<Config>();
		res.addAll(this.configRepository.findAll());
		
		return res.get(0);
	}

	public Config save(final Config config) {
		Assert.notNull(config, "The config to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to save a config.");
		
		final Config res = this.configRepository.save(config);
		return res;
	}

	public void delete(final Config config) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete a config.");

		Assert.notNull(config, "The category to delete cannot be null.");
		Assert.isTrue(this.configRepository.exists(config.getId()));

		
		this.configRepository.delete(config);
	}
	
	//------------------Utility Methdos--------------------

}