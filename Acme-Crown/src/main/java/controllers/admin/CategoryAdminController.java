/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/admin")
public class CategoryAdminController extends AbstractController {
	
	@Autowired
	private CategoryService categoryService;
	
	// Constructors -----------------------------------------------------------

	public CategoryAdminController() {
		super();
	}

	// Actions ---------------------------------------------------------------	
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		Collection<Category> categories = this.categoryService.findAll();
		
		result = new ModelAndView("category/list");
		result.addObject("categories", categories);
		result.addObject("requestURI", "/category/admin/list.do");

		return result;
	}
	
	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		
		Category category = this.categoryService.create();
		
		result = new ModelAndView("category/create");
		result.addObject("category", category);

		return result;
	}
	
	@RequestMapping(value="/edit",method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int categoryId) {
		ModelAndView result;
		
		Category category = this.categoryService.findOne(categoryId);
		
		result = new ModelAndView("category/edit");
		result.addObject("category", category);

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(Category category, BindingResult binding) {
		ModelAndView result;
		
		this.categoryService.validate(category, binding);
		if(!binding.hasErrors()){
			try{
				this.categoryService.reconstructAndSave(category);
				result = new ModelAndView("redirect:list.do");
				
			}catch(Throwable oops){
				if(category.getId()==0){
					result = new ModelAndView("category/create");
				}else{
					result = new ModelAndView("category/edit");
				}
				result.addObject("category", category);
				result.addObject("message", "category.commint.error");
			}
		}else{
			if(category.getId()==0){
				result = new ModelAndView("category/create");
			}else{
				result = new ModelAndView("category/edit");
			}
			result.addObject("category", category);
		}
		
		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="delete")
	public ModelAndView edit(Category category) {
		ModelAndView result;
		
		try{
			Category res = this.categoryService.findOne(category.getId());
			this.categoryService.delete(res);
			
			Collection<Category> categories = this.categoryService.findAll();
			
			result = new ModelAndView("category/list");
			result.addObject("categories", categories);
			result.addObject("requestURI", "/category/admin/list.do");
			result.addObject("message", "category.delete.success");
			
		}catch(Throwable oops){
			result = new ModelAndView("category/edit");
			result.addObject("category", category);
			result.addObject("message", "category.commint.error");
		}
		
		return result;
	}
	
	
}
