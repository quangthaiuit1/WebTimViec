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
import javax.persistence.criteria.Root;

import lixco.com.entities.Keyword;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class KeywordService extends AbstractService<Keyword>{
	
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	@Override
	protected Class<Keyword> getEntityClass() {
		return Keyword.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	@Override
	public Keyword findById(long id) {
		return null;
	}
	
	public List<Keyword> findByNameContaining(String name){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Keyword> cq = cb.createQuery(Keyword.class);
		Root<Keyword> root = cq.from(Keyword.class);
		cq.select(root).where(cb.like(root.get("name"), (name + "%")));
		TypedQuery<Keyword> query = em.createQuery(cq);
		return query.getResultList();
	}
//	public Post findByIdAfterJoin(long id) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Post> cq = cb.createQuery(Post.class);
//		Root<Post> root = cq.from(Post.class);
//		root.fetch("listJob",JoinType.INNER);
//		root.fetch("location",JoinType.INNER);
//		root.fetch("listKeyword",JoinType.INNER);
//		root.fetch("company",JoinType.INNER);
//		cq.select(root).where(cb.equal(root.get("id"), id));
//		TypedQuery<Post> query = em.createQuery(cq);
//		List<Post> results = query.getResultList();
//		if(results.size() != 0) {
//			return results.get(0);
//		}
//		else return null;
//	}
//	public List<Post> findAllByJoin(){
//		try {
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<Post> cq = cb.createQuery(Post.class);
//			Root<Post> root = cq.from(Post.class);
//			root.fetch("listJob",JoinType.INNER);
//			root.fetch("location",JoinType.INNER);
//			root.fetch("listKeyword",JoinType.INNER);
//			root.fetch("company",JoinType.INNER);
//			cq.select(root);
//			cq.orderBy(cb.asc(root.get("id")));
//			TypedQuery<Post> query = em.createQuery(cq);
//			List<Post> t=query.getResultList();
//			return t;
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

}
