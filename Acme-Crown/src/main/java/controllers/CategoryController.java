package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;

import domain.Category;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController{
	
	@Autowired
	private CategoryService categoryService;
	
	// Constructors -----------------------------------------------------------

	public CategoryController() {
		super();
	}
	
	// Actions ---------------------------------------------------------------	
	
	@RequestMapping(value="/icontable",method = RequestMethod.GET)
	public ModelAndView icontable() {
		ModelAndView result;
		
		Collection<Category> categories = this.categoryService.findAll();
		
		result = new ModelAndView("category/icontable");
		result.addObject("categories", categories);
		result.addObject("requestURI", "/category/icontable.do");

		return result;
	}

}
