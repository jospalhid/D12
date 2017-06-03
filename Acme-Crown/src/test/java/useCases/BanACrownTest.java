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
import services.ModeratorService;
import utilities.AbstractTest;
import domain.Crown;
import domain.Moderator;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BanACrownTest extends AbstractTest{
	
	/* *----Banear un crown-----*
	  -El orden de los parámetros es: Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test:
	  		//El usuario autenticado es un moderador y puede bloquear (test positivo)
			//El usuario no está autenticado y por lo tanto no puede bloquear (test negativo)
				
	 */
	
	@Autowired
	private CrownService crownService;
	
	@Autowired
	private ModeratorService moderatorService;
	
	private List<Moderator> moderators;
	
	@Before
    public void setup() {
		this.moderators= new ArrayList<Moderator>();
		this.moderators.addAll(this.moderatorService.findAll());
		
		Collections.shuffle(this.moderators);
	}
	
	@Test
	public void driver() {
		final Object testingData[][] = {
			{
				this.moderators.get(0).getUserAccount().getUsername(), null
			}, {
				"noExiste", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	

	protected void template(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Moderator m = moderatorService.findByUserAccountId(LoginService.getPrincipal().getId());
			
			m.setLevel(2);
			List<Crown> crowns = new ArrayList<Crown>();
			crowns.addAll(crownService.findAllNotBanned());
			Collections.shuffle(crowns);
			
			if(!crowns.isEmpty()){
				Crown c = crowns.get(0);
				this.crownService.ban(c.getId());
			}
			
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
