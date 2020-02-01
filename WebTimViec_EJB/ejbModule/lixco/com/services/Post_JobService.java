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

import lixco.com.entities.Post_Job;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class Post_JobService extends AbstractService<Post_Job>{

	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	@Override
	protected Class<Post_Job> getEntityClass() {
		return Post_Job.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	//Tim kiem nhap vao toan bo
	public List<Post_Job> find(String name, String job, String location){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Post_Job> cq = cb.createQuery(Post_Job.class);
		Root<Post_Job> root = cq.from(Post_Job.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("post").get("isDeleted"), false);
		queries.add(deleteQuery);
		if(name != null && !name.equals("")) {
			Predicate nameQuery = cb.like(root.get("post").get("name"), (name + "%"));
			queries.add(nameQuery);
		}
		
		if(job != null && !job.equals("")) {
			Predicate jobQuery =cb.equal(root.get("job").get("name"), job);
			queries.add(jobQuery);
		}
		
		if(location != null && !location.equals("")) {
			Predicate locationQuery = cb.equal(root.get("post").get("location").get("name"), location);
			queries.add(locationQuery);
		}
		
		Predicate data[] = new Predicate[queries.size()];
		for(int i = 0; i< queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate
		  = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Post_Job> query = em.createQuery(cq);
		return query.getResultList();
	}

}
