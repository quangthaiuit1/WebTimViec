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

}
