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

import lixco.com.entities.Post_Keyword;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class Post_KeywordService extends AbstractService<Post_Keyword>{
	
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	@Override
	protected Class<Post_Keyword> getEntityClass() {
		return Post_Keyword.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public List<Post_Keyword> findByPostId(long postId){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Post_Keyword> cq = cb.createQuery(Post_Keyword.class);
		Root<Post_Keyword> root = cq.from(Post_Keyword.class);
		cq.select(root).where(cb.equal(root.get("post").get("id"), postId));
		TypedQuery<Post_Keyword> query = em.createQuery(cq);
		List<Post_Keyword> post_keyword = query.getResultList();
		return post_keyword;
	}
}
