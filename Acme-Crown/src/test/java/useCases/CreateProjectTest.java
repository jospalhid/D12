package useCases;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import services.AdminService;
import services.CategoryService;
import services.CrownService;
import services.ModeratorService;
import services.ProjectService;
import utilities.AbstractTest;
import domain.Admin;
import domain.Category;
import domain.Crown;
import domain.Moderator;
import domain.Project;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateProjectTest extends AbstractTest {

	/*
	 * Crear un poyecto - Manager
	 *
	 * -El orden de los parámetros es: Usuario (Crown) que se va a autenticar,título,
	 * descripcion, objetivo, tiempo de vida y Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe y se puede crear el nuevo proyecto(test positivo)
	 * -El usuario no está autenticado y no se puede crear un nuevo proyecto(test negativo)
	 * -El usuario es un administrador y no se puede crear un nuevo proyecto(test negativo)
	 * -El usuario es un moderador y no se puede crear un nuevo proyecto(test negativo)
	 * -El título está vacío por lo que no se puede crear un proyecto(test negativo)
	 * -El objetivo es negativo por lo que no se puede crear un proyecto(test negativo)
	 * -El objetivo es 0 por lo que se puede crear un proyecto(test positivo)
	 * -El objetivo es una unidad por debajo de lo permitido por lo que no se puede 
	 * crear un proyecto(test negativo)
	 *  -El objetivo es una unidad por encima de lo permitido por lo que se puede 
	 * crear un proyecto(test positivo)

	 */
	@Autowired
	private CrownService crownService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ModeratorService moderatorService;

	
	private List<Crown> crowns;
	private List<Category> categories;
	private List<Admin> admins;
	private List<Moderator> moderators;
	
	@Before
    public void setup() {
		this.crowns= new ArrayList<Crown>();
		this.crowns.addAll(this.crownService.findAll());
		
		Collections.shuffle(this.crowns);
		
		this.categories = new ArrayList<Category>();
		this.categories.addAll(this.categoryService.findAll());
		
		Collections.shuffle(this.categories);
		
		this.admins = new ArrayList<Admin>();
		this.admins.addAll(this.adminService.findAll());
		
		Collections.shuffle(this.admins);
		
		this.moderators = new ArrayList<Moderator>();
		this.moderators.addAll(this.moderatorService.findAll());
		
		Collections.shuffle(moderators);
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",30,categories.get(0), null },
				{null,"titulo","descripcion",30,categories.get(0),IllegalArgumentException.class},
				{this.admins.get(0).getUserAccount().getUsername(),"titulo","descripcion",30,categories.get(0),IllegalArgumentException.class},
				{this.moderators.get(0).getUserAccount().getUsername(),"titulo","descripcion",30,categories.get(0),IllegalArgumentException.class},
				{this.crowns.get(0).getUserAccount().getUsername(),"","descripcion",70,categories.get(0), IllegalArgumentException.class},
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",-70,categories.get(0), IllegalArgumentException.class},
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",0,categories.get(0), null},
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",-1,categories.get(0), IllegalArgumentException.class},
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",1,categories.get(0), null},

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(int) testingData[i][3],
					(Category) testingData[i][4],
					(Class<?>) testingData[i][5]);
	}

	protected void template(String username,String title, String description, int goal,
			 Category category,final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Project p= projectService.create(crownService.findByUserAccountId(LoginService.getPrincipal().getId()), category);
			p.setTitle(title);
			p.setDescription(description);
			p.setGoal(goal);
			Calendar time= Calendar.getInstance();
			time.set(2017, 06, 30);
			
			p.setTtl(time.getTime());

			projectService.save(p);
			
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
