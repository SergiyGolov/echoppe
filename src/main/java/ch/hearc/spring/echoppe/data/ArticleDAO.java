package ch.hearc.spring.echoppe.data;

import java.util.List;

import javax.validation.Valid;

import ch.hearc.spring.echoppe.model.Article;

public interface ArticleDAO {

	List<Article> findAll();
	
	Article findById(long id);
	
	Article findByName(String name);

	void save(@Valid Article product);

}