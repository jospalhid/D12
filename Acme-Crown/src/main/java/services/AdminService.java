package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Admin;
import domain.Sms;

@Service
@Transactional
public class AdminService {

	//Managed repository
	@Autowired
	private AdminRepository	adminRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

	//Constructors
	public AdminService() {
		super();
	}

	//Simple CRUD methods
	public Admin create(final UserAccount ua) {
		Admin res;
		res = new Admin();
		res.setUserAccount(ua);
		res.setReceivedMessages(new ArrayList<Sms>());
		res.setSendMessages(new ArrayList<Sms>());
		return res;
	}

	public Collection<Admin> findAll() {
		final Collection<Admin> res = this.adminRepository.findAll();
		return res;
	}

	public Admin findOne(final int adminId) {
		final Admin res = this.adminRepository.findOne(adminId);
		return res;
	}

	public Admin save(final Admin admin) {
		Assert.notNull(admin, "The admin to save cannot be null.");
		
		final Admin res = this.adminRepository.save(admin);
		return res;
	}

	public void delete(final Admin admin) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(admin, "The admin to delete cannot be null.");
		Assert.isTrue(this.adminRepository.exists(admin.getId()));
		
		Assert.isTrue(admin.getReceivedMessages().isEmpty(), "Cannot delete with messages");
		Assert.isTrue(admin.getSendMessages().isEmpty(), "Cannot delete with messages");

		this.adminRepository.delete(admin);
	}

	//Utilites methods
	public Admin findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.adminRepository.findByUserAccountId(id);
		}

}