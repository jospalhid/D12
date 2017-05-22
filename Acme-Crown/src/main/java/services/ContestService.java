package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ContestRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Contest;
import forms.ContestForm;

@Service
@Transactional
public class ContestService {

	//Managed repository
	@Autowired
	private ContestRepository	contestRepository;


	//Validator
//	@Autowired
//	private Validator validator;
	
	//Supporting services

	//Constructors
	public ContestService() {
		super();
	}

	//Simple CRUD methods
	public Contest create() {
		Contest res;
		res = new Contest();
		return res;
	}

	public Collection<Contest> findAll() {
		final Collection<Contest> res = this.contestRepository.findAll();
		return res;
	}

	public Contest findOne(final int contestId) {
		final Contest res = this.contestRepository.findOne(contestId);
		return res;
	}

	public Contest save(final Contest contest) {
		Assert.notNull(contest, "The crown to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to create a contest.");
		
		final Contest res = this.contestRepository.save(contest);
		return res;
	}
	public Contest saveAndEdit(final Contest contest) {
		Assert.notNull(contest, "The crown to save cannot be null.");
		
		final Contest res = this.contestRepository.save(contest);
		return res;
	}

	public void delete(final Contest contest) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete a contest.");

		Assert.notNull(contest, "The contest to delete cannot be null.");
		Assert.isTrue(this.contestRepository.exists(contest.getId()));
		
		Assert.isTrue(contest.getProjects().isEmpty(), "The contest cannot be delete with proyects");

		this.contestRepository.delete(contest);
	}

	//Utilites methods
	public Collection<Contest> findAvailableContest(){
		return this.contestRepository.findAvailableContest();
	}

	public Contest join(ContestForm contestForm) {
		Contest contest = this.findOne(contestForm.getContestId());
		contest.getProjects().add(contestForm.getProject());
		return this.saveAndEdit(contest);
	}

}