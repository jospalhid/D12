package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ExtraRewardRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.ExtraReward;
import domain.Project;

@Service
@Transactional
public class ExtraRewardService {

	//Managed repository
	@Autowired
	private ExtraRewardRepository	extraRewardRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services
	@Autowired
	private ProjectService projectService;

	//Constructors
	public ExtraRewardService() {
		super();
	}

	//Simple CRUD methods
	public ExtraReward create(Project project) {
		ExtraReward res;
		res = new ExtraReward();
		res.setProject(project);
		return res;
	}

	public Collection<ExtraReward> findAll() {
		final Collection<ExtraReward> res = this.extraRewardRepository.findAll();
		return res;
	}

	public ExtraReward findOne(final int extraRewardId) {
		final ExtraReward res = this.extraRewardRepository.findOne(extraRewardId);
		return res;
	}

	public ExtraReward save(final ExtraReward extraReward) {
		Assert.notNull(extraReward, "The extra reward to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CROWN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a crown to create a reward.");
		
		Assert.isTrue(extraReward.getProject().getCrown().getUserAccount().equals(ua), "You are not the owner of the project");
		
		//Comprobar que se ha completado el objetivo del projecto
		Assert.isTrue(extraReward.getProject().getGoal()<=this.projectService.getCurrentGoal(extraReward.getProject().getId()), "The goal must be reached");
		
		final ExtraReward res = this.extraRewardRepository.save(extraReward);
		return res;
	}

	public void delete(final ExtraReward reward) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a customer to delete a reward.");

		Assert.notNull(reward, "The reward to delete cannot be null.");
		Assert.isTrue(this.extraRewardRepository.exists(reward.getId()));
		
		Assert.isTrue(reward.getProject().getCrown().getUserAccount().equals(ua), "You are not the owner of the project");

		this.extraRewardRepository.delete(reward);
	}

	//Utilites methods

}