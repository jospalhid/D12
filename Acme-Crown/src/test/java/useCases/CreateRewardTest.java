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
import services.CrownService;
import services.RewardService;
import utilities.AbstractTest;
import domain.Crown;
import domain.Project;
import domain.Reward;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateRewardTest extends AbstractTest {

	/*
	 * Create an event - Manager
	 *
	 * -El orden de los parámetros es: Usuario (Manager) que se va a autenticar,
	 * titulo, descripcion, coste y Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe y se puede crear la nueva recompensa(test positivo)
	 * -El usuario no está autenticado y no se puede crear la nueva recompensa(test negativo)
	*/
	@Autowired
	private CrownService crownService;
	
	@Autowired
	private RewardService rewardService;
	
	
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
				{this.crowns.get(0).getUserAccount().getUsername(),"titulo","descripcion",300.0,null },
				{null,"titulo","descripcion",300.0,IllegalArgumentException.class},
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(double) testingData[i][3],
					(Class<?>) testingData[i][4]);
	}

	protected void template(String username,String titulo, String description, double cost,
			final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Crown c= crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			
			List<Project> projects = new ArrayList<Project>();
			projects.addAll(c.getProjects());
			
			if(!projects.isEmpty()){
				Project p = projects.get(0);
				
				Reward r= rewardService.create(p);
				r.setCost(cost);
				r.setTitle(titulo);
				r.setDescription(description);
				
				rewardService.save(r);	
			}
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
