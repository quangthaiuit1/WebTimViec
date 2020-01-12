package lixco.com.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.entities.Company;
import lixco.com.entities.User;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class UserService extends AbstractService<User>{

	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	
	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public User login(String username,String password) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		List<Predicate> queries = new ArrayList<Predicate>();
		Predicate isDeleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(isDeleteQuery);
		if(!username.equals("") && password == null) {
			Predicate predicateForUsername
			  = cb.equal(root.get("username"), username);
			queries.add(predicateForUsername);
		}
		if(!username.equals("") && password != null) {
			Predicate predicateForUsername
			  = cb.equal(root.get("username"), username);
			Predicate predicateForPassword
			  = cb.equal(root.get("password"), password);
			queries.add(predicateForUsername);
			queries.add(predicateForPassword);
		}
		Predicate data[] = new Predicate[queries.size()];
		for(int i = 0; i< queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate
		  = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<User> query = em.createQuery(cq);
		List<User> temp = query.getResultList();
		if(temp.size() != 0) {
			return temp.get(0);
		}
		else return null;
	}
	public User registration(User user, Company newCompany) {
		User newUser = new User();
		newUser = user;
		newUser.setCompany(newCompany);
		return this.create(newUser);
	}
}
