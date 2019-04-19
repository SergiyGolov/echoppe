package ch.hearc.spring.echoppe.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import ch.hearc.spring.echoppe.model.Article;
import ch.hearc.spring.echoppe.model.Command;
import ch.hearc.spring.echoppe.repository.ArticleRepository;
import ch.hearc.spring.echoppe.repository.CommandRepository;

@Controller
public class ArticleController {

	@Autowired
	private ArticleRepository arepo;

	@Autowired
	private CommandRepository crepo;

	@GetMapping(value = "/articles")
	public String findAllarticles(Map<String, Object> model) {
		System.out.println("/articles GET");
		model.put("articles", arepo.findAll());
		model.put("article", new Article());

		return "articles";
	}

	@GetMapping(value = "/article/{id}")
	public String findArticle(@PathVariable("id") long id, Model model) {
		model.addAttribute("article", arepo.findById(id));
		return "article";
	}

	@GetMapping(value = "/input_articles")
	public String inputArticles(Map<String, Object> model) {

		model.put("article", new Article());

		return "input_articles";
	}

	@PostMapping("/articles")
	public String savearticles(Article article, BindingResult errors, Model model) {

		if (!errors.hasErrors()) {
			arepo.save(article);
		}
		return ((errors.hasErrors()) ? "input_articles" : "redirect:articles");
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/payment/{id}")
	public String findPayment(@PathVariable("id") long id, Model model, HttpServletRequest request) {

		Command command = crepo.findById(id);

		if (command == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La commande spécifiée n'a pas été trouvée");

		Principal principal = request.getUserPrincipal();

		String currentUserName = principal.getName();

		String commandUserName = command.getUtilisateur().getUsername();

		// Workaround, otherwise currentUserName==commandUserName didn't work
		if (commandUserName.compareTo(currentUserName) == 0) {
			model.addAttribute("command", command);
			return "payment";
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La commande spécifiée ne vous concerne pas");
		}

	}
}
