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

import lixco.com.entities.Location;

@Stateless
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class LocationService extends AbstractService<Location>{
	
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	
	@Override
	protected Class<Location> getEntityClass() {
		return Location.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	public List<Location> findByNameContaining(String name){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Location> cq = cb.createQuery(Location.class);
		Root<Location> root = cq.from(Location.class);
		cq.select(root).where(cb.like(root.get("name"), (name + "%")));
		TypedQuery<Location> query = em.createQuery(cq);
		return query.getResultList();
	}
}
