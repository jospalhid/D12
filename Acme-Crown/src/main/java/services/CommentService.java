
package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import security.LoginService;
import domain.Comment;
import domain.Crown;
import domain.Project;

@Service
@Transactional
public class CommentService {

	//Managed repository
	@Autowired
	private CommentRepository	commentRepository;

	//Suppoert services
	@Autowired
	private CrownService		crownService;

	@Autowired
	private Validator			validator;


	//Constructors
	public CommentService() {
		super();
	}

	//Simple CRUD methods
	public Comment create(final Crown sender, final Project project) {
		Assert.notNull(sender, "The sender cannot be null");
		Assert.notNull(project, "The recipient cannot be null");
		Comment res;
		res = new Comment();
		res.setCrown(sender);
		res.setProject(project);
		res.setMoment(Calendar.getInstance().getTime());
		res.setStars(0);
		res.setBanned(false);

		return res;
	}

	public Comment findOne(final int commentId) {
		final Comment res = this.commentRepository.findOne(commentId);
		return res;
	}

	public Collection<Comment> findAll() {
		final Collection<Comment> res = this.commentRepository.findAll();
		return res;
	}

	public Comment save(final Comment comment) {
		Assert.notNull(comment, "The comment to save cannot be null.");
		Assert.notNull(comment.getCrown());

		Assert.isTrue(comment.getMoment() != null);
		Assert.isTrue(comment.getTitle() != null);
		Assert.isTrue(comment.getText() != null);
		Assert.isTrue(comment.getStars() >= 0 && comment.getStars() <= 5, "The stars must be between 1 and 5");

		final Comment res = this.commentRepository.save(comment);
		res.getCrown().getPostComments().add(res);
		res.getProject().getComments().add(res);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public Collection<Comment> findReceivedComments(final int projectId) {
		return this.commentRepository.findReceivedComments(projectId);
	}

	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		final Crown sender = this.crownService.findByUserAccountId(LoginService.getPrincipal().getId());

		final Comment res = this.create(sender, comment.getProject());
		res.setTitle(comment.getTitle());
		res.setText(comment.getText());
		res.setStars(comment.getStars());

		this.validator.validate(res, binding);

		return res;
	}
}
