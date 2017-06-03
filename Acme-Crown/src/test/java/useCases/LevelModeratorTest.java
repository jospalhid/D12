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

import services.AdminService;
import services.ModeratorService;
import utilities.AbstractTest;
import domain.Admin;
import domain.Moderator;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LevelModeratorTest extends AbstractTest {

	/*
	 * Cambiar el rango de un moderador - Admin
	 *
	 * -El orden de los parámetros es: Usuario (Manager) que se va a autenticar, Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe(test positivo)
	 * -El usuario no está autenticado(test negativo)
	 */
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ModeratorService moderatorService;

	
	private List<Admin> admins;
	private List<Moderator> moderators;
	
	@Before
    public void setup() {
		this.admins= new ArrayList<Admin>();
		this.admins.addAll(this.adminService.findAll());
		
		Collections.shuffle(this.admins);
		
		this.moderators = new ArrayList<Moderator>();
		this.moderators.addAll(this.moderatorService.findAll());
		
		Collections.shuffle(this.moderators);
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.admins.get(0).getUserAccount().getUsername(), null },
				{"", IllegalArgumentException.class}
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
	}

	protected void template(String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Moderator m = moderators.get(0);
			m.setLevel(2);
			
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
