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

import lixco.com.entities.Permission;
import lixco.com.entities.Post;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class PermissionService extends AbstractService<Permission>{
	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	@Override
	protected Class<Permission> getEntityClass() {
		return Permission.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	public List<Permission> findAllByFilter(){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
			Root<Permission> root = cq.from(Permission.class);
			Predicate predicateForIsDeleted
			  = cb.equal(root.get("isDeleted"), false);
			cq.where(predicateForIsDeleted);
			TypedQuery<Permission> query = em.createQuery(cq);
			List<Permission> t = query.getResultList();
			return t;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
