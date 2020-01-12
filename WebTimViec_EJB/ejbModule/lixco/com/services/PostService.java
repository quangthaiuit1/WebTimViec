package lixco.com.services;

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

import lixco.com.entities.Post;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class PostService extends AbstractService<Post>{
	
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	@Override
	protected Class<Post> getEntityClass() {
		return Post.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	//Tim kiem theo ten Post
	public List<Post> findByNamePost(String input){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Post> cq = cb.createQuery(Post.class);
		Root<Post> root = cq.from(Post.class);
		cq.select(root).where(cb.like(root.get("name"), (input + "%")));
		TypedQuery<Post> query = em.createQuery(cq);
		return query.getResultList();
	}
	
	public List<Post> findAllByFilter(){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Post> cq = cb.createQuery(Post.class);
			Root<Post> root = cq.from(Post.class);
			Predicate predicateForIsDeleted
			  = cb.equal(root.get("isDeleted"), false);
			cq.where(predicateForIsDeleted);
			TypedQuery<Post> query = em.createQuery(cq);
			List<Post> t = query.getResultList();
			return t;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Post> findAllByCustomer(long userId){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Post> cq = cb.createQuery(Post.class);
			Root<Post> root = cq.from(Post.class);
			Predicate predicateForIsDeleted
			  = cb.equal(root.get("isDeleted"), false);
			Predicate predicateForCompanyId
			  = cb.equal(root.get("user").get("id"), userId);
			Predicate finalPredicate
			  = cb.and(predicateForIsDeleted,predicateForCompanyId );
			cq.where(finalPredicate);
			TypedQuery<Post> query = em.createQuery(cq);
			List<Post> t = query.getResultList();
			return t;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
