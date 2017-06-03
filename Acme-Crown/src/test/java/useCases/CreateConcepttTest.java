package useCases;

import java.util.ArrayList;
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
import services.ConceptService;
import services.CrownService;
import utilities.AbstractTest;
import domain.Concept;
import domain.Crown;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateConcepttTest extends AbstractTest {

	/*
	 * Crear una idea - Crown
	 *
	 * -El orden de los parámetros es: Usuario (Crown) que se va a autenticar, titulo,
	 * descripcion, tiempo de vida, minimo y Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe y se puede crear el nuevo proyecto(test positivo)
	 * -El usuario no está autenticado y no se puede crear un nuevo proyecto(test negativo)
	 */
	@Autowired
	private CrownService crownService;
	
	@Autowired
	private ConceptService conceptService;

	
	private List<Crown> crowns;
	
	@Before
    public void setup() {
		this.crowns= new ArrayList<Crown>();
		this.crowns.addAll(this.crownService.findAll());
		
		Collections.shuffle(this.crowns);
		
		
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",23,350.0, null },
				{null,"titulo","descripcion",23,350.0, IllegalArgumentException.class},
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(int) testingData[i][3],
					(double) testingData[i][4],
					(Class<?>) testingData[i][5]);
	}

	protected void template(String username,String title, String description, int ttl,
			double minimo, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Crown cr = crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			Concept c = conceptService.create(cr);
			c.setTitle(title);
			c.setDescripcion(description);
			c.setTtl(ttl);
			c.setDibs(minimo);

			conceptService.save(c);
			
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
