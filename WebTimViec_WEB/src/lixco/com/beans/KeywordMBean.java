package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import lixco.com.entities.Keyword;

import lixco.com.services.KeywordService;


@ManagedBean
@ViewScoped
public class KeywordMBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Keyword> keywords;

	@Inject
	private KeywordService keywordService;
		
	
	@PostConstruct
	public void init(){
		keywords = new ArrayList<>();
		keywords = keywordService.findAll();
	}
	
	public List<Keyword> completeTheme(String input){
		String queryLowerCase = input.toLowerCase();
		List<Keyword> temp = new ArrayList<>();
		temp = keywordService.findByNameContaining(input);
		return temp.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
}
