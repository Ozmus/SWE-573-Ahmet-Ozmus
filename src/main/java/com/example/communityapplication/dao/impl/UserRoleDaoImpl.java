package com.example.communityapplication.dao.impl;


import com.example.communityapplication.dao.UserRoleDao;
import com.example.communityapplication.enums.Role;
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
	public List<UserRole> findByCommunityId(int communityId) {
		TypedQuery<UserRole> theQuery = entityManager.createQuery(
				"from UserRole where id.communityId = :communityId",
				UserRole.class
		);
		theQuery.setParameter("communityId", communityId);

		return theQuery.getResultList();
	}

	@Override
	public List<UserRole> findOwnersByCommunityId(int communityId) {
		TypedQuery<UserRole> theQuery = entityManager.createQuery(
				"from UserRole where id.communityId = :communityId and role = :urRole",
				UserRole.class
		);
		theQuery.setParameter("communityId", communityId);
		theQuery.setParameter("urRole", Role.OWNER);


		return theQuery.getResultList();	}

	@Override
	@Transactional
	public void updateUserRole(UserRole userRole, Role updatedUserRole) {
		if (userRole != null) {
			// Update the existing user role entity with the new values from updatedUserRole
			userRole.setRole(updatedUserRole);

			// Merge the updated entity back to the database
			entityManager.merge(userRole);
		}
	}

	@Override
	public void deleteUserRole(UserRole userRole) {
		entityManager.remove(userRole);
	}

	@Override
	@Transactional
	public void save(UserRole theUserRole) {
		entityManager.merge(theUserRole);
	}
}
