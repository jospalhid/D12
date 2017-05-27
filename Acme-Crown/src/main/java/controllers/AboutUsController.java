
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/aboutUs")
public class AboutUsController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public AboutUsController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/acme", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		try {
			result = new ModelAndView("welcome/aboutUs");
		} catch (Throwable oops) {

			result = new ModelAndView("hacker/hackers");

		}
		return result;
	}
}