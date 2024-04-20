package com.example.communityapplication.dao.impl;


import com.example.communityapplication.dao.UserRoleDao;
import com.example.communityapplication.model.User;
import com.example.communityapplication.model.UserRole;
import com.example.communityapplication.model.embedded.keys.UserRolesId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

	private EntityManager entityManager;

	@Autowired
	public UserRoleDaoImpl(EntityManager theEntityManager) {
		this.entityManager = theEntityManager;
	}

	@Override
	public UserRole findByUserAndCommunityId(UserRolesId userRolesId) {
		TypedQuery<UserRole> theQuery = entityManager.createQuery("from UserRole where id=:uRoleId", UserRole.class);
		theQuery.setParameter("uRoleId", userRolesId);
		UserRole theUserRole = null;
		try {
			theUserRole = theQuery.getSingleResult();
		} catch (Exception e) {
			theUserRole = null;
		}

		return theUserRole;
	}
	@Override
	public List<UserRole> findByUserId(long userId) {
		TypedQuery<UserRole> theQuery = entityManager.createQuery(
				"from UserRole where id.userId = :userId",
				UserRole.class
		);
		theQuery.setParameter("userId", userId);

		return theQuery.getResultList();
	}

	@Override
	@Transactional
	public void save(UserRole theUserRole) {
		entityManager.merge(theUserRole);
	}
}
