package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Category;
import domain.Project;

@Service
@Transactional
public class CategoryService {

	//Managed repository
	@Autowired
	private CategoryRepository	categoryRepository;


	//Validator
	@Autowired
	private Validator validator;
	
	//Supporting services

	//Constructors
	public CategoryService() {
		super();
	}

	//Simple CRUD methods
	public Category create() {
		Category res;
		res = new Category();
		res.setProjects(new ArrayList<Project>());
		return res;
	}

	public Collection<Category> findAll() {
		final Collection<Category> res = this.categoryRepository.findAll();
		return res;
	}

	public Category findOne(final int categoryId) {
		final Category res = this.categoryRepository.findOne(categoryId);
		return res;
	}

	public Category save(final Category category) {
		Assert.notNull(category, "The crown to save cannot be null.");
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to create a category.");
		
		final Category res = this.categoryRepository.save(category);
		return res;
	}

	public void delete(final Category category) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(category, "The category to delete cannot be null.");
		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		Assert.isTrue(category.getProjects().isEmpty(), "The category cannot be delete with projects");
		
		this.categoryRepository.delete(category);
	}

	public Category validate(Category category, BindingResult binding) {
		Category res = this.create();
		res.setName(category.getName());
		res.setDescription(category.getDescription());
		
		validator.validate(res, binding);
		
		return res;
	}

	public Category reconstructAndSave(Category category) {
		Category res;
		if(category.getId()!=0){
			res=this.findOne(category.getId());
		}else{
			res=this.create();
		}
		
		res.setName(category.getName());
		res.setDescription(category.getDescription());
		
		Category save = this.save(res);
		
		return save;
	}

	//Utilites methods

}