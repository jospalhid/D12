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

import services.CommentService;
import services.ModeratorService;
import services.ProjectService;
import utilities.AbstractTest;
import domain.Comment;
import domain.Moderator;
import domain.Project;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BanACommentTest extends AbstractTest{
	
	/* *----Banear un comentario-----*
	  -El orden de los parámetros es: Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test:
	  		//El usuario autenticado es un moderador y puede bloquear (test positivo)
			//El usuario no está autenticado y por lo tanto no puede bloquear (test negativo)
				
	 */
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ModeratorService moderatorService;

	@Autowired
	private ProjectService projectService;
	
	private List<Moderator> moderators;
	private List<Project> projects;
	
	@Before
    public void setup() {
		this.moderators= new ArrayList<Moderator>();
		this.moderators.addAll(this.moderatorService.findAll());
		
		Collections.shuffle(this.moderators);
		
		this.projects = new ArrayList<Project>();
		this.projects.addAll(this.projectService.findAll());
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
			
		
			
			if(!projects.isEmpty()){
				Project p = projects.get(0);
				List<Comment> comments = new ArrayList<Comment>();
				comments.addAll(p.getComments());
				if(!comments.isEmpty()){
					this.commentService.ban(comments.get(0));
				}

			}
			
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
