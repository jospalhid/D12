package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Service
@Transactional
public class UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;
				
	//Supporting services
				
	//Constructors
	public UserAccountService() {
		super();
	}
				
	//Simple CRUD methods
	public UserAccount create() {
		UserAccount res;
		res = new UserAccount();
		res.setAuthorities(new ArrayList<Authority>());
		return res;
	}
	
	public Collection<UserAccount> findAll() {
		Collection<UserAccount> res = userAccountRepository.findAll();
		return res;
	}
	
	public UserAccount findOne(int id){
		return userAccountRepository.findOne(id);
	}

	public UserAccount save(UserAccount ua) {
		Assert.notNull(ua, "The user account to save cannot be null.");
		//Assert.isTrue(userAccountRepository.findUserNames().contains(ua.getUsername()), "The user account already exists");	
		
		UserAccount res = userAccountRepository.save(ua);
		return res;
	}
}
