/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.crown;

import java.util.Calendar;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ProjectService;
import controllers.AbstractController;
import domain.Project;
import forms.ProjectForm;

@Controller
@RequestMapping("/project/crown")
public class ProjectCrownController extends AbstractController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CategoryService categoryService;

	// Constructors -----------------------------------------------------------

	public ProjectCrownController() {
		super();
	}

	// Actions ---------------------------------------------------------------		

	@RequestMapping(value="/create",method = RequestMethod.GET)
	public ModelAndView available() {
		ModelAndView result;
		
		ProjectForm project = new ProjectForm();

		result = new ModelAndView("project/create");
		result.addObject("project", project);
		result.addObject("categories", this.categoryService.findAll());

		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params="save")
	public ModelAndView edit(@Valid ProjectForm project, BindingResult binding) {
		ModelAndView result;
		
		Project res= this.projectService.reconstruct(project, binding);
		if(!binding.hasErrors()){
			try{
				this.projectService.save(res);
				
				//TODO Redireccionar al display
				Collection<Project> projects = this.projectService.findAvailableProjects();

				result = new ModelAndView("project/available");
				result.addObject("projects", projects);
				result.addObject("current", Calendar.getInstance().getTimeInMillis()/86400000);
				result.addObject("requestURI", "projects/available.do");
			}
			catch(Throwable oops){
				if(project.getId()==0){
					result = new ModelAndView("project/create");
				}else{
					result = new ModelAndView("project/edit");
				}
				result.addObject("project", project);
				result.addObject("categories", this.categoryService.findAll());
				result.addObject("message", "project.commit.error");
			}
		}else{
			if(project.getId()==0){
				result = new ModelAndView("project/create");
			}else{
				result = new ModelAndView("project/edit");
			}
			result.addObject("project", project);
			result.addObject("categories", this.categoryService.findAll());
			result.addObject("message", "project.commit.incomplete");
		}
		

		return result;
	}
	
	

}
