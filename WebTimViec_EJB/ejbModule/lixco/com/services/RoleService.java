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

import lixco.com.entities.Role;
import lixco.com.entities.User;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class RoleService extends AbstractService<Role>{
	
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	
	@Override
	protected Class<Role> getEntityClass() {
		return Role.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	public List<Role> findAllByFilter(){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Role> cq = cb.createQuery(Role.class);
			Root<Role> root = cq.from(Role.class);
			Predicate predicateForIsDeleted
			  = cb.equal(root.get("isDeleted"), false);
			cq.where(predicateForIsDeleted);
			TypedQuery<Role> query = em.createQuery(cq);
			List<Role> t = query.getResultList();
			return t;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
