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

import lixco.com.entities.Job;
import lixco.com.entities.Location;
import lixco.com.entities.Post;

@Stateless
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class JobService extends AbstractService<Job>{
	
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	
	@Override
	protected Class<Job> getEntityClass() {
		return Job.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	public List<Job> findByNameContaining(String name){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Job> cq = cb.createQuery(Job.class);
		Root<Job> root = cq.from(Job.class);
		cq.select(root).where(cb.like(root.get("name"), (name + "%")));
		TypedQuery<Job> query = em.createQuery(cq);
		return query.getResultList();
	}
	public List<Job> findAllByFilter(){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Job> cq = cb.createQuery(Job.class);
			Root<Job> root = cq.from(Job.class);
			Predicate predicateForIsDeleted
			  = cb.equal(root.get("isDeleted"), false);
			cq.where(predicateForIsDeleted);
			TypedQuery<Job> query = em.createQuery(cq);
			List<Job> t = query.getResultList();
			return t;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
