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

import lixco.com.entities.User_Role;


@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class User_RoleService extends AbstractService<User_Role>{

	@PersistenceContext
	private EntityManager em;
	 
	@Resource
	private SessionContext ct;
	@Override
	protected Class<User_Role> getEntityClass() {
		return User_Role.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public List<User_Role> findByUserId(long userId){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User_Role> cq = cb.createQuery(User_Role.class);
		Root<User_Role> root = cq.from(User_Role.class);
		cq.select(root).where(cb.equal(root.get("user").get("id"), userId));
		TypedQuery<User_Role> query = em.createQuery(cq);
		List<User_Role> rolePermissions = query.getResultList();
		return rolePermissions;
	}
}
