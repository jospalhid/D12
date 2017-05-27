
package controllers.crown;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CommentService;
import services.CrownService;
import services.ProjectService;
import controllers.AbstractController;
import domain.Comment;
import domain.Crown;
import domain.Project;

@Controller
@RequestMapping("/comment/crown")
public class CommentCrownController extends AbstractController {

	@Autowired
	private CrownService	crownService;
	@Autowired
	private CommentService	commentService;
	@Autowired
	private ProjectService	projectService;


	@RequestMapping(value = "/post", method = RequestMethod.GET)
	public ModelAndView request(@RequestParam final int projectId) {
		ModelAndView result;

		final Crown sender = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());

		final Project project = this.projectService.findOne(projectId);

		final Comment res = this.commentService.create(sender, project);
		res.setStars(2);

		result = new ModelAndView("comment/post");
		result.addObject("comment", res);

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView search(final Comment comment, final BindingResult binding) {
		ModelAndView result;
		final Comment res = this.commentService.reconstruct(comment, binding);
		if (!binding.hasErrors())
			try {
				this.commentService.save(res);
				final int projectId = res.getProject().getId();
				final Project project = this.projectService.findOne(projectId);
				final Long days = this.projectService.getDaysToGo(projectId);
				final Integer brackers = this.projectService.getBackers(projectId);
				final Crown crown = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());
				final Collection<Comment> comments = this.commentService.findReceivedComments(projectId);

				result = new ModelAndView("project/display");
				result.addObject("project", project);
				Double currentGoal = this.projectService.getCurrentGoal(projectId);
				if (currentGoal == null)
					currentGoal = 0.0;
				result.addObject("currentGoal", currentGoal);
				result.addObject("days", days);
				result.addObject("brackers", brackers);
				result.addObject("crown", crown);
				result.addObject("comments", comments);
				result.addObject("requestURI", "/project/display.do?projectId=" + projectId);
				if (crown != null)
					if (crown.getFavs().contains(project))
						result.addObject("fav", true);
					else
						result.addObject("fav", false);

			} catch (final Throwable opps) {
				result = new ModelAndView("comment/post");
				result.addObject("comment", comment);
				result.addObject("message", "project.commit.error");
			}
		else {

			result = new ModelAndView("comment/post");
			result.addObject("comment", comment);

		}

		return result;
	}

}
