package ch.hearc.spring.echoppe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ch.hearc.spring.echoppe.model.Article;
import ch.hearc.spring.echoppe.model.Command;
import ch.hearc.spring.echoppe.model.Comment;
import ch.hearc.spring.echoppe.model.Payment;
import ch.hearc.spring.echoppe.model.Rating;
import ch.hearc.spring.echoppe.model.Role;
import ch.hearc.spring.echoppe.model.Utilisateur;
import ch.hearc.spring.echoppe.repository.ArticleRepository;
import ch.hearc.spring.echoppe.repository.CommentRepository;
import ch.hearc.spring.echoppe.repository.RatingRepository;
import ch.hearc.spring.echoppe.repository.RoleRepository;
import ch.hearc.spring.echoppe.repository.UserRepository;

@SpringBootApplication
public class EchoppeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EchoppeApplication.class, args);
	}

	@Autowired
	private UserRepository utilisateurRepo;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private ArticleRepository articleRepo;

	@Autowired
	private RatingRepository ratingRepo;

	@Autowired
	private CommentRepository commentRepo;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@PostConstruct
	public void initData() {
		// Role
		Role roleAdmin = new Role("ROLE_ADMIN");
		Role roleUser = new Role("ROLE_USER");
		roleRepo.save(roleAdmin);
		roleRepo.save(roleUser);

		// User
		Utilisateur admin = new Utilisateur("admin", "admin@test.ch", bCryptPasswordEncoder.encode("password"));
		Utilisateur user = new Utilisateur("user", "user@test.ch", bCryptPasswordEncoder.encode("password"));

		Set<Role> adminRoles = new HashSet<Role>();
		adminRoles.add(roleAdmin);
		adminRoles.add(roleUser);

		admin.addRoles(adminRoles);
		user.addRole(roleUser);
		utilisateurRepo.save(admin);
		utilisateurRepo.save(user);

		// Article
		Article article1 = new Article("Banane", 2.4);
		Article article2 = new Article("Crayon", 0.5);
		// TODO: add categories to articles
		articleRepo.save(article1);
		articleRepo.save(article2);

		// Rating
		Rating rating1 = new Rating(5, user, article1);
		Rating rating2 = new Rating(2, user, article2);
		ratingRepo.save(rating1);
		ratingRepo.save(rating2);

		// Comment
		Comment comment1 = new Comment("C'est plutôt pas mal", user, article1);
		Comment comment2 = new Comment("Vraiment nul !", user, article2);
		commentRepo.save(comment1);
		commentRepo.save(comment2);

		// Payment
		Payment payment1 = null;
		Payment payment2 = null;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			payment1 = new Payment(user, 1, sdf.parse("04-04-2019 10:30:00"), 0);
			payment2 = new Payment(user, 1, sdf.parse("04-04-2019 10:30:00"), 0);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Command
		Command command1 = new Command();
		Command command2 = new Command();
		command1.addContent(article1, 5);
		command1.addContent(article2, 2);
		command2.addContent(article1, 3);
		command2.addContent(article2, 7);
		command1.setPayment(payment1);
		command2.setPayment(payment2);

	}

}
