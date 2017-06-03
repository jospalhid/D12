package useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import services.CategoryService;
import services.CrownService;
import services.ProjectService;
import utilities.AbstractTest;
import domain.Category;
import domain.Crown;
import domain.Project;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateProjectTest extends AbstractTest {

	/*
	 * Create an event - Manager
	 *
	 * -El orden de los parámetros es: Usuario (Manager) que se va a autenticar,título,
	 * descripcion, objetivo, tiempo de vida y Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe y se puede crear el nuevo proyecto(test positivo)
	 * -El usuario no está autenticado y no se puede crear un nuevo proyecto(test negativo)
	 */
	@Autowired
	private CrownService crownService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CategoryService categoryService;

	
	private List<Crown> crowns;
	private List<Category> categories;
	
	@Before
    public void setup() {
		this.crowns= new ArrayList<Crown>();
		this.crowns.addAll(this.crownService.findAll());
		
		Collections.shuffle(this.crowns);
		
		this.categories = new ArrayList<Category>();
		this.categories.addAll(this.categoryService.findAll());
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",30,new Date(2017/05/30),new Date(2017/05/28),categories.get(0), null },
				{null,"titulo","descripcion",30,new Date(2017/05/30),new Date(2017/05/28),categories.get(0), null },
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(int) testingData[i][3],
					(Date) testingData[i][4],
					(Date) testingData[i][5],
					(Category) testingData[i][6],
					(Class<?>) testingData[i][7]);
	}

	protected void template(String username,String title, String description, int goal,
			Date ttl, Date moment,Category category,final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Project p= projectService.create(crownService.findByUserAccountId(LoginService.getPrincipal().getId()), category);
			p.setTitle(title);
			p.setDescription(description);
			p.setGoal(goal);
			p.setTtl(ttl);
			p.setMoment(moment);

			projectService.save(p);
			
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
